package bftProxyServer;
import bftsmart.reconfiguration.util.RSAKeyUtils;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.RequestVerifier;
import bftsmart.tom.server.defaultservices.DefaultReplier;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import bftsmart.tom.util.TOMUtil;

import org.json.JSONObject;

import java.io.*;
import java.security.PrivateKey;
import java.util.Iterator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankService extends DefaultSingleRecoverable {

    private BankRepository bankRepo;
    private int id;

    private final Logger logger;

    public BankService(int replicaID) {
        this.id = replicaID;
        this.bankRepo = new BankRepository();
        logger = Logger.getLogger(BankService.class.getName());
        ServiceReplica s = new ServiceReplica(replicaID, "config",this, this,(RequestVerifier)null, new DefaultReplier());
        
        this.bankRepo.load(id);
    }

	public static void main(String[] args){
        if(args.length < 1) {

        } else {
            new BankService(Integer.parseInt(args[0]));
        }
    }


    @Override
    public byte[] appExecuteOrdered(byte[] commandBytes, MessageContext messageContext) {

        logger.info("Ordered");

        byte[] requestReply = null;
        byte[] finalReply = null;

        boolean hasRequestReply = false;

        try
        (

                ByteArrayInputStream receivedRequestByteArrayInputStream = new ByteArrayInputStream(commandBytes);

                ObjectInput receivedRequestObjectInput = new ObjectInputStream(receivedRequestByteArrayInputStream);

                ByteArrayOutputStream requestReplyByteArrayOutputStream = new ByteArrayOutputStream();

                ObjectOutput requestReplyObjectOutput = new ObjectOutputStream(requestReplyByteArrayOutputStream)

        )
        {
            String bankServiceRequestType = receivedRequestObjectInput.readObject().toString();
            String username;
            String who;

            String password;

            String from;
            String to;

            Long amount;

            switch (bankServiceRequestType) {

                case "REGISTER_USER":

                    username = (String) receivedRequestObjectInput.readObject();
                    if ( !this.bankRepo.findByUserName(username).isPresent() ) {

                        password = (String) receivedRequestObjectInput.readObject(); // TODO no plaintext here!!!!
                        amount = (Long) receivedRequestObjectInput.readObject();
                        BankEntity bankEntity = BankServiceHelper.registerUser(username, password, amount, bankRepo);
                        if ( bankEntity != null ) {
                            logger.info("Created new user (on replica "+this.id+"): "+bankEntity.getOwnerName());
                            
                            try {
							
                                requestReplyObjectOutput.writeObject( bankEntity.getOwnerName() );
                                hasRequestReply = true;
                            	
                            }
                            catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                            

                        }

                    }

                    break;

                case "CREATE_MONEY":

                    who = (String) receivedRequestObjectInput.readObject();
                    logger.info(who);
                    if ( this.bankRepo.findByUserName(who).isPresent() ) {

                        amount = (Long) receivedRequestObjectInput.readObject();
                        logger.info(who);
                        logger.info(String.valueOf(amount));
                        JSONObject jsonObject = BankServiceHelper.createMoney(who, amount, bankRepo);

            requestReplyObjectOutput.writeObject(jsonObject.toString());

            hasRequestReply = true;

        }

        break;

        case "TRANSFER_MONEY":

        from = (String) receivedRequestObjectInput.readObject();

        Optional<BankEntity> beFrom = bankRepo.findByUserName(from);

        to = (String) receivedRequestObjectInput.readObject();

        Optional<BankEntity> beTo = bankRepo.findByUserName(to);

        if ( ( beFrom.isPresent() ) && ( beTo.isPresent() ) ) {

            amount = (Long) receivedRequestObjectInput.readObject();

            if ( ( amount > 0 ) && ( ( beFrom.get().getAmount() - amount ) >= 0 ) ) {

                JSONObject jsonObject = BankServiceHelper.transferMoney(from, to, amount, bankRepo);

                requestReplyObjectOutput.writeObject(jsonObject.toString());

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

        
		try {
		
			PrivateKey privateKey = RSAKeyUtils.getPrivateKeyFromString("config/keys/privatekey" + this.id);
		
			byte[] signature = TOMUtil.signMessage(privateKey, requestReply);
		
			finalReply = new byte[requestReply.length + 20];

			System.arraycopy(requestReply, 0, finalReply, 0, requestReply.length);
			System.arraycopy(signature, 0, finalReply, requestReply.length, 20);
			
		}
		catch (Exception exception) {
		
			exception.printStackTrace();
		
		}
    	
    	
        
    }
            else {

            	//requestReply = new byte[0];
            	finalReply = new byte[0];

    }

}
        catch (IOException | ClassNotFoundException executeOrderedException) {

            logger.log
                    (
                            Level.SEVERE, "Occurred an exception during an Execution of an Ordered Operation",
                            executeOrderedException
                    );

        }
        bankRepo.save(this.id);
        return finalReply;

    }

    @Override
    public byte[] appExecuteUnordered(byte[] commandBytes, MessageContext messageContext) {

        logger.info("UnOrdered");
        byte[] requestReply = null;
        byte[] finalReply = null;

        boolean hasRequestReply = false;

        try
        (

                ByteArrayInputStream receivedRequestByteArrayInputStream = new ByteArrayInputStream(commandBytes);

                ObjectInput receivedRequestObjectInput = new ObjectInputStream(receivedRequestByteArrayInputStream);

                ByteArrayOutputStream requestReplyByteArrayOutputStream = new ByteArrayOutputStream();

                ObjectOutput requestReplyObjectOutput = new ObjectOutputStream(requestReplyByteArrayOutputStream)

        )
        {

            String bankServiceRequestType =
                     receivedRequestObjectInput.readObject().toString();

            String username;

            String who;
            Long amount;

            switch (bankServiceRequestType) {

                case "FIND_USER":
                    username = (String) receivedRequestObjectInput.readObject();

                    if ( this.bankRepo.findByUserName(username).isPresent() ) {

                        BankEntity userBankEntity = BankServiceHelper.findUser(username, bankRepo);

                        requestReplyObjectOutput.writeObject(userBankEntity.getJSON().toString());
                        logger.info("User tried to login: "+username);
                        hasRequestReply = true;

                    }

                    break;

                case "LIST_ALL_BANK_ACCOUNTS":

                    Iterator<BankEntity> it = bankRepo.iterator();
                    //Iterable<BankEntity> usersBankEntities = BankServiceHelper.getAllBankAcc(bankRepo);

                    int numTotalUserBankEntities = bankRepo.getSize();

                    requestReplyObjectOutput.writeObject(numTotalUserBankEntities);

                    while(it.hasNext()) {
                        BankEntity i = it.next();
                        requestReplyObjectOutput.writeObject(i.getJSONSecure().toString());
                    }
                    /*for ( BankEntity userBankEntity: usersBankEntities ) {

                        requestReplyObjectOutput.writeObject(userBankEntity.getJSONSecure().toString());
                    }*/

                    hasRequestReply = true;

                    break;

                case "CHECK_CURRENT_AMOUNT":

                    who = (String) receivedRequestObjectInput.readObject();

                    if ( this.bankRepo.findByUserName(who).isPresent() ) {

                        amount = BankServiceHelper.currentAmount(who, bankRepo);

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
                
                try {
            		
        			PrivateKey privateKey = RSAKeyUtils.getPrivateKeyFromString("config/keys/privatekey" + this.id);
        		
        			byte[] signature = TOMUtil.signMessage(privateKey, requestReply);
        		
        			finalReply = new byte[requestReply.length + 20];

        			System.arraycopy(requestReply, 0, finalReply, 0, requestReply.length);
        			System.arraycopy(signature, 0, finalReply, requestReply.length, 20);
        			
        		}
        		catch (Exception exception) {
        		
        			exception.printStackTrace();
        		
        		}

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
        bankRepo.save(this.id);
        return finalReply;

    }

    @Override
    public byte[] getSnapshot() {

        try
        (
                ByteArrayOutputStream snapshotByteArrayOutputStream = new ByteArrayOutputStream();

                ObjectOutput snapshotObjectOutput = new ObjectOutputStream(snapshotByteArrayOutputStream)
        )
        {

            snapshotObjectOutput.writeObject(this.bankRepo.version);

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

            this.bankRepo.version = (int) snapshotObjectInput.readObject();

        }
        catch (IOException | ClassNotFoundException snapshotException) {

            logger.log(Level.SEVERE, "Error while installing Snapshot", snapshotException);

        }

    }

}