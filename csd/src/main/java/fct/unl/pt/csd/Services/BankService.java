package fct.unl.pt.csd.Services;

import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Enumerations.BankServiceRequestType;
import fct.unl.pt.csd.Repositories.BankRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

@Service
public class BankService extends DefaultSingleRecoverable {

    private BankRepo bankRepo;

    private final Logger logger;


    @Autowired
    public BankService(int replicaID, BankRepo bankRepo) {
        this.bankRepo = bankRepo;
        logger = Logger.getLogger(BankService.class.getName());
        new ServiceReplica(replicaID, this, this);

    }

    @Override
    public byte[] appExecuteOrdered(byte[] commandBytes, MessageContext messageContext) {

        byte[] requestReply = null;

        boolean hasRequestReply = false;

        try
        (

                ByteArrayInputStream receivedRequestByteArrayInputStream = new ByteArrayInputStream(commandBytes);

                ObjectInput receivedRequestObjectInput = new ObjectInputStream(receivedRequestByteArrayInputStream);

                ByteArrayOutputStream requestReplyByteArrayOutputStream = new ByteArrayOutputStream();

                ObjectOutput requestReplyObjectOutput = new ObjectOutputStream(requestReplyByteArrayOutputStream)

        )
        {

            BankServiceRequestType bankServiceRequestType =
                            (BankServiceRequestType) receivedRequestObjectInput.readObject();

            String username;
            String who;

            String password;

            String from;
            String to;

            Long amount;

            switch (bankServiceRequestType) {

                case REGISTER_USER:

                    username = (String) receivedRequestObjectInput.readObject();

                    if ( !this.bankRepo.findByUserName(username).isPresent() ) {

                        password = (String) receivedRequestObjectInput.readObject(); // TODO no plaintext here!!!!

                        amount = (Long) receivedRequestObjectInput.readObject();

                        BankEntity bankEntity = BankServiceHelper.registerUser(username, password, amount, bankRepo);

                        if ( bankEntity != null ) {

                            requestReplyObjectOutput.writeObject( bankEntity );

                            hasRequestReply = true;

                        }

                    }

                    break;

                case CREATE_MONEY:

                    who = (String) receivedRequestObjectInput.readObject();

                    if ( this.bankRepo.findByUserName(who).isPresent() ) {

                        amount = (Long) receivedRequestObjectInput.readObject();

                        JSONObject jsonObject = BankServiceHelper.createMoney(who, amount, bankRepo);

                        requestReplyObjectOutput.writeObject(jsonObject);

                        hasRequestReply = true;

                    }

                    break;

                case TRANSFER_MONEY:

                    from = (String) receivedRequestObjectInput.readObject();

                    Optional<BankEntity> beFrom = bankRepo.findByUserName(from);

                    to = (String) receivedRequestObjectInput.readObject();

                    Optional<BankEntity> beTo = bankRepo.findByUserName(to);

                    if ( ( beFrom.isPresent() ) && ( beTo.isPresent() ) ) {

                        amount = (Long) receivedRequestObjectInput.readObject();

                        if ( ( amount > 0 ) && ( ( beFrom.get().getAmount() - amount ) >= 0 ) ) {

                            JSONObject jsonObject = BankServiceHelper.transferMoney(from, to, amount, bankRepo);

                            requestReplyObjectOutput.writeObject(jsonObject);

                            hasRequestReply = true;

                        }

                    }

                    break;

                default:

                    logger.log(Level.WARNING, "Unsupported Ordered Operation!!!");

            }

            if (hasRequestReply) {

                requestReplyObjectOutput.flush();
                requestReplyByteArrayOutputStream.flush();

                requestReply = requestReplyByteArrayOutputStream.toByteArray();

            }
            else {

                requestReply = new byte[0];

            }

        }
        catch (IOException | ClassNotFoundException executeOrderedException) {

            logger.log
                    (
                            Level.SEVERE, "Occurred an exception during an Execution of an Ordered Operation",
                            executeOrderedException
                    );

        }

        return requestReply;

    }

    @Override
    public byte[] appExecuteUnordered(byte[] commandBytes, MessageContext messageContext) {

        byte[] requestReply = null;

        boolean hasRequestReply = false;

        try
        (

                ByteArrayInputStream receivedRequestByteArrayInputStream = new ByteArrayInputStream(commandBytes);

                ObjectInput receivedRequestObjectInput = new ObjectInputStream(receivedRequestByteArrayInputStream);

                ByteArrayOutputStream requestReplyByteArrayOutputStream = new ByteArrayOutputStream();

                ObjectOutput requestReplyObjectOutput = new ObjectOutputStream(requestReplyByteArrayOutputStream)

        )
        {

            BankServiceRequestType bankServiceRequestType =
                    (BankServiceRequestType) receivedRequestObjectInput.readObject();

            String username;

            String who;

            switch (bankServiceRequestType) {

                case FIND_USER:

                    username = (String) receivedRequestObjectInput.readObject();

                    if ( this.bankRepo.findByUserName(username).isPresent() ) {

                        BankEntity userBankEntity = BankServiceHelper.findUser(username, bankRepo);

                        requestReplyObjectOutput.writeObject(userBankEntity);

                        hasRequestReply = true;

                    }

                    break;

                case LIST_ALL_BANK_ACCOUNTS:

                    Iterable<BankEntity> usersBankEntities = this.getAllBankAcc();

                    int numTotalUserBankEntities =
                            (int) StreamSupport.stream(usersBankEntities.spliterator(), false).count();

                    requestReplyObjectOutput.writeObject(numTotalUserBankEntities);

                    for ( BankEntity userBankEntity: usersBankEntities ) {

                        requestReplyObjectOutput.writeObject(userBankEntity);

                    }

                    hasRequestReply = true;

                    break;

                case CHECK_CURRENT_AMOUNT:

                    who = (String) receivedRequestObjectInput.readObject();

                    if ( this.bankRepo.findByUserName(who).isPresent() ) {

                        long amount = BankServiceHelper.currentAmount(who, bankRepo);

                        requestReplyObjectOutput.writeObject(amount);

                        hasRequestReply = true;

                    }

                    break;

                default:

                    logger.log(Level.WARNING, "Unsupported Unordered Operation!!!");

            }

            if (hasRequestReply) {

                requestReplyObjectOutput.flush();
                requestReplyByteArrayOutputStream.flush();

                requestReply = requestReplyByteArrayOutputStream.toByteArray();

            }
            else {

                requestReply = new byte[0];

            }

        }
        catch (IOException | ClassNotFoundException executeOrderedException) {

            logger.log
                    (
                            Level.SEVERE, "Occurred an exception during an Execution of an Unordered Operation",
                            executeOrderedException
                    );

        }

        return requestReply;

    }

    @Override
    public byte[] getSnapshot() {

        try
        (
                ByteArrayOutputStream snapshotByteArrayOutputStream = new ByteArrayOutputStream();

                ObjectOutput snapshotObjectOutput = new ObjectOutputStream(snapshotByteArrayOutputStream)
        )
        {

            snapshotObjectOutput.writeObject(this.bankRepo);

            return snapshotByteArrayOutputStream.toByteArray();

        }
        catch (IOException snapshotException) {

            logger.log(Level.SEVERE, "Error while taking Snapshot", snapshotException);

        }

        return new byte[0];

    }

    @Override
    public void installSnapshot(byte[] snapshotStateBytes) {

        try
        (

                ByteArrayInputStream snapshotByteArrayInputStream = new ByteArrayInputStream(snapshotStateBytes);

                ObjectInput snapshotObjectInput = new ObjectInputStream(snapshotByteArrayInputStream)

        )
        {

            this.bankRepo = (BankRepo) snapshotObjectInput.readObject();

        }
        catch (IOException | ClassNotFoundException snapshotException) {

            logger.log(Level.SEVERE, "Error while installing Snapshot", snapshotException);

        }

    }




}