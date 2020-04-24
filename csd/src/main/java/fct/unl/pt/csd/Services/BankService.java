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

                        BankEntity bankEntity = this.registerUser(username, password, amount);

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

                        JSONObject jsonObject = this.createMoney(who, amount);

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

                            JSONObject jsonObject = this.transferMoney(from, to, amount);

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

                        BankEntity userBankEntity = this.findUser(username);

                        requestReplyObjectOutput.writeObject(userBankEntity);

                        hasRequestReply = true;

                    }

                    break;

                case LIST_ALL_BANK_ACCOUNTS:

                    for ( BankEntity userBankEntity: this.getAllBankAcc() ) {

                        requestReplyObjectOutput.writeObject(userBankEntity);

                    }

                    hasRequestReply = true;

                    break;

                case CHECK_CURRENT_AMOUNT:

                    who = (String) receivedRequestObjectInput.readObject();

                    if ( this.bankRepo.findByUserName(who).isPresent() ) {

                        long amount = this.currentAmount(who);

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


    public BankEntity registerUser(String username, String password, Long amount) {

        if(!bankRepo.findByUserName(username).isPresent()) {

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            return bankRepo.save(new BankEntity(username, passwordEncoder.encode(password), amount));

        }
        else {

            return null;

        }

    }

    public BankEntity findUser(String username) {

        if ( this.bankRepo.findByUserName(username).isPresent() ) {

            return this.bankRepo.findByUserName(username).get();

        }

        return null;

    }

    public Iterable<BankEntity> getAllBankAcc(){

        return bankRepo.findAll();

    }

    public JSONObject transferMoney(String from, String to, long amount) {

        Optional<BankEntity> beFrom = bankRepo.findByUserName(from);

        if(beFrom.isPresent()){

            Optional<BankEntity> beTo = bankRepo.findByUserName(to);

            if(beTo.isPresent()){

                if(amount > 0) {

                    BankEntity b = beFrom.get();

                    if ( ( b.getAmount() - amount ) >= 0 ) {

                        b.updateAmount(-amount);

                        bankRepo.save(b);
                        b = beTo.get();

                        b.updateAmount(amount);
                        bankRepo.save(b);

                        return new JSONObject().put("Success", "True");

                    }
                    else {

                        return new JSONObject().put("error", "From account doesn't have enough money").put("errorID", 3);

                    }
                }
                else {

                    return new JSONObject().put("error", "Amount<0").put("errorID",2);

                }

            }
            else {

                return new JSONObject().put("error", "To account doesn't exist").put("errorID",1);

            }

        }
        else {

            return new JSONObject().put("error", "From account doesn't exist").put("errorID",0);

        }

    }

    public JSONObject createMoney(String who,long amount){

        Optional<BankEntity> be = bankRepo.findByUserName(who);

        if( be.isPresent() ) {

            BankEntity b = be.get();

            b.updateAmount(b.getAmount()+amount);
            bankRepo.save(b);

            return new JSONObject().put("Success","True").put("amount",b.getAmount());

        }
        else

            return new JSONObject().put("error","User not found "+who);

    }

    public long currentAmount(String who) {

        Optional<BankEntity> be = bankRepo.findByUserName(who);

        if (be.isPresent()) {

            BankEntity b = be.get();

            return b.getAmount();

        }
        else {

            return -1;

        }

    }

}