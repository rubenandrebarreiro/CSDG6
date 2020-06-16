package bftProxyServer;

import bftsmart.reconfiguration.util.RSAKeyUtils;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.RequestVerifier;
import bftsmart.tom.server.defaultservices.DefaultReplier;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import org.json.JSONArray;
import org.json.JSONObject;
import src.SmartContract;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Iterator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankService extends DefaultSingleRecoverable {

    protected volatile BankRepository bankRepo;
    protected int id;

    protected final Logger logger;
    private SmartContractRunner sR;

    public BankService(int replicaID) {
        this.id = replicaID;
        this.bankRepo = new BankRepository();
        logger = Logger.getLogger(BankService.class.getName());
        ServiceReplica s = new ServiceReplica(replicaID, "config", this, this, (RequestVerifier) null, new DefaultReplier());
        this.sR = new SmartContractRunner(this);
        Thread t = new Thread(sR);
        t.start();

        this.bankRepo.load(id);
    }

    public static void main(String[] args) {
        if (args.length < 1) {

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

                ) {
            String bankServiceRequestType = receivedRequestObjectInput.readObject().toString();
            String username;
            String who;

            String password;

            String from;
            String to;

            Long amount;
            Long auctionID, bidID;

            Optional<BankEntity> bankEnt;
            Optional<AuctionEntity> auctionEnt;
            Optional<AuctionEntity> openedAuctionEnt;
            Optional<AuctionEntity> closedAuctionEnt;
            Optional<BidEntity> bidEnt;
            logger.info(bankServiceRequestType);
            switch (bankServiceRequestType) {

                case "REGISTER_USER":

                    username = (String) receivedRequestObjectInput.readObject();
                    if (!this.bankRepo.findByUserName(username).isPresent()) {

                        password = (String) receivedRequestObjectInput.readObject(); // TODO no plaintext here!!!!
                        amount = (Long) receivedRequestObjectInput.readObject();
                        String[] roles = (String[]) receivedRequestObjectInput.readObject();
                        BankEntity bankEntity = BankServiceHelper.registerUser(username, password, amount, roles, bankRepo);
                        if (bankEntity != null) {
                            logger.info("Created new user (on replica " + this.id + "): " + bankEntity.getOwnerName());
                            requestReplyObjectOutput.writeObject(bankEntity.getOwnerName());
                            hasRequestReply = true;

                        }

                    }

                    break;

                case "CREATE_MONEY":
                    Iterator<BankEntity> it = bankRepo.usersIterator();
                    //Iterable<bftProxyServer.BankEntity> usersBankEntities = bftProxyServer.BankServiceHelper.getAllBankAcc(bankRepo);

                    int hash = 0;
                    JSONArray arr = new JSONArray();

                    while (it.hasNext()) {
                        BankEntity i = it.next();
                        hash = hash ^ i.getJSONSecure().toString().hashCode();
                        arr.put(i.getJSONSecure());
                    }
                    logger.info(arr.toString());
                    who = (String) receivedRequestObjectInput.readObject();
                    logger.info(who);
                    if (this.bankRepo.findByUserName(who).isPresent()) {
                        Long original = receivedRequestObjectInput.readLong();
                        amount = receivedRequestObjectInput.readLong();
                        logger.info(who);
                        logger.info(String.valueOf(amount));
                        JSONObject jsonObject;
                        Long orig = bankRepo.compareAmount(who, original);
                        System.out.println(who + " " + original);
                        if (orig >= 0)
                            jsonObject = BankServiceHelper.createMoney(who, amount, bankRepo);
                        else
                            jsonObject = new JSONObject().put("error", "Original amount did not pass verifications").put("amount", -orig);
                        requestReplyObjectOutput.writeObject(jsonObject.toString());

                        hasRequestReply = true;

                    }

                    break;

                case "TRANSFER_MONEY":

                    from = (String) receivedRequestObjectInput.readObject();
                    long fromSaldo = receivedRequestObjectInput.readLong();
                    to = (String) receivedRequestObjectInput.readObject();
                    long toSaldo = receivedRequestObjectInput.readLong();

                    long fromAmount = receivedRequestObjectInput.readLong();
                    long toAmount = receivedRequestObjectInput.readLong();
                    System.out.println("From amount final" + fromAmount);
                    System.out.println("To amount final" + toAmount);

                    Optional<BankEntity> beFrom = bankRepo.findByUserName(from);
                    Optional<BankEntity> beTo = bankRepo.findByUserName(to);
                    JSONObject jsonObject = new JSONObject();

                    if ((beFrom.isPresent()) && (beTo.isPresent())) {
                        Long sFrom = beFrom.get().getAmount();
                        Long sTo = beTo.get().getAmount();

                        if (beFrom.get().getAmount() == fromSaldo && beTo.get().getAmount() == toSaldo) {

                            jsonObject = BankServiceHelper.transferMoney(from, fromAmount, to, toAmount, bankRepo);
                        } else {
                            jsonObject.put("error", true).put("fromSaldo", sFrom).put("toSaldo", sTo);
                        }
                        System.out.println(jsonObject.toString());
                        requestReplyObjectOutput.writeObject(jsonObject.toString());

                        hasRequestReply = true;

                    }

                    break;

                case "CREATE_AUCTION":

                    who = (String) receivedRequestObjectInput.readObject();

                    bankEnt = bankRepo.findByUserName(who);

                    auctionID = (Long) receivedRequestObjectInput.readObject();

                    auctionEnt = bankRepo.findByAuctionID(auctionID);

                    if (bankEnt.isPresent()) {

                        if (!auctionEnt.isPresent()) {

                            jsonObject = BankServiceHelper.createAuction(auctionID, who, bankRepo);

                            requestReplyObjectOutput.writeObject(jsonObject.toString());

                            hasRequestReply = true;

                        }

                    }

                    break;

                case "CLOSE_AUCTION":
                    who = (String) receivedRequestObjectInput.readObject();
                    auctionID = (Long) receivedRequestObjectInput.readObject();
                    logger.info("Closing down this auction ("+auctionID+")");
                    jsonObject = BankServiceHelper.closeAution(auctionID, who, bankRepo);
                    requestReplyObjectOutput.writeObject(jsonObject.toString());

                    hasRequestReply = true;
                    break;

                case "CREATE_SMART_CONTRACT":
                    who = (String) receivedRequestObjectInput.readObject();

                    bankEnt = bankRepo.findByUserName(who);

                    int sizeOfCode = receivedRequestObjectInput.readInt();
                    byte[] code = new byte[sizeOfCode];
                    receivedRequestObjectInput.readFully(code);

                    if (bankEnt.isPresent()) {

                        jsonObject = new JSONObject();
                        ClassLoader c = new ClassLoader();
                        SmartContract sC = null;
                        try {
                            sC = (SmartContract) c.createObjectFromFile(who, code);
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        if (sC != null) {
                            jsonObject = BankServiceHelper.validateCode(sC.getClass());
                            if (!jsonObject.has("error"))
                                sR.runContract(sC, who);
                        }

                        requestReplyObjectOutput.writeObject(jsonObject.toString());

                        hasRequestReply = true;

                    }
                    break;


                default:
                    //logger.log(Level.WARNING, "Unsupported ordered Operation!!!");
                    return appExecuteUnordered(commandBytes, messageContext);

            }

            if (hasRequestReply) {

                requestReplyObjectOutput.flush();
                requestReplyByteArrayOutputStream.flush();

                requestReply = requestReplyByteArrayOutputStream.toByteArray();

                try {
                    byte[] encoded = Files.readAllBytes(Paths.get("config/keys/privatekey" + this.id));
                    String key = new String(encoded);
                    PrivateKey privateKey = RSAKeyUtils.getPrivateKeyFromString(key);
//                    byte[] signature = TOMUtil.signMessage(privateKey, requestReply);
                    Signature sha_rsa = Signature.getInstance("SHA512withRSA");
                    sha_rsa.initSign(privateKey);
                    sha_rsa.update(requestReply);
                    byte[] signature = sha_rsa.sign();

                    finalReply = new byte[requestReply.length + 128];

                    System.arraycopy(requestReply, 0, finalReply, 0, requestReply.length);
                    System.arraycopy(signature, 0, finalReply, requestReply.length, 128);

                } catch (Exception exception) {

                    exception.printStackTrace();

                }

            } else {

                requestReply = new byte[0];

            }

        } catch (IOException | ClassNotFoundException executeOrderedException) {

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

                ) {

            String bankServiceRequestType =
                    receivedRequestObjectInput.readObject().toString();

            String username;

            String who;
            Long amount;

            Long auctionID;
            logger.info(bankServiceRequestType);

            switch (bankServiceRequestType) {

                case "FIND_USER":
                    username = (String) receivedRequestObjectInput.readObject();

                    if (this.bankRepo.findByUserName(username).isPresent()) {

                        BankEntity userBankEntity = BankServiceHelper.findUser(username, bankRepo);

                        requestReplyObjectOutput.writeObject(userBankEntity.getJSON().toString());
                        logger.info("User tried to login: " + username);
                        hasRequestReply = true;

                    }

                    break;

                case "LIST_ALL_BANK_ACCOUNTS":

                    Iterator<BankEntity> it = bankRepo.usersIterator();
                    //Iterable<bftProxyServer.BankEntity> usersBankEntities = bftProxyServer.BankServiceHelper.getAllBankAcc(bankRepo);

                    int hash = 0;
                    JSONArray arr = new JSONArray();

                    while (it.hasNext()) {
                        BankEntity i = it.next();
                        hash = hash ^ i.getJSONSecure().toString().hashCode();
                        arr.put(i.getJSONSecure());
                    }
                    requestReplyObjectOutput.writeObject(hash);
//                    requestReplyObjectOutput.writeObject(numTotalUserBankEntities);
                    requestReplyObjectOutput.writeObject(arr.toString());
                    /*for ( bftProxyServer.BankEntity userBankEntity: usersBankEntities ) {

                        requestReplyObjectOutput.writeObject(userBankEntity.getJSONSecure().toString());
                    }*/

                    hasRequestReply = true;

                    break;

                case "CHECK_CURRENT_AMOUNT":

                    who = (String) receivedRequestObjectInput.readObject();

                    if (this.bankRepo.findByUserName(who).isPresent()) {

                        amount = BankServiceHelper.currentAmount(who, bankRepo);

                        requestReplyObjectOutput.writeObject(amount);

                        hasRequestReply = true;

                    }

                    break;

                case "LIST_ALL_CURRENT_OPENED_AUCTIONS":

                    Iterator<AuctionEntity> openedAuctionsIt = bankRepo.openedAuctionsIterator();
                    //Iterable<bftProxyServer.BankEntity> usersBankEntities = bftProxyServer.BankServiceHelper.getAllBankAcc(bankRepo);

                    int numTotalOpenedAuctionsBankEntities = bankRepo.getNumOpenedAuctions();

                    requestReplyObjectOutput.writeObject(numTotalOpenedAuctionsBankEntities);

                    while (openedAuctionsIt.hasNext()) {
                        AuctionEntity i = openedAuctionsIt.next();
                        requestReplyObjectOutput.writeObject(i.getJSON().toString());
                    }
                    /*for ( bftProxyServer.BankEntity userBankEntity: usersBankEntities ) {

                        requestReplyObjectOutput.writeObject(userBankEntity.getJSONSecure().toString());
                    }*/

                    hasRequestReply = true;

                    break;

                case "LIST_ALL_CURRENT_CLOSED_AUCTIONS":

                    Iterator<AuctionEntity> closedAuctionsIt = bankRepo.closedAuctionsIterator();
                    //Iterable<bftProxyServer.BankEntity> usersBankEntities = bftProxyServer.BankServiceHelper.getAllBankAcc(bankRepo);

                    int numTotalClosedAuctionsBankEntities = bankRepo.getNumOpenedAuctions();

                    requestReplyObjectOutput.writeObject(numTotalClosedAuctionsBankEntities);

                    while (closedAuctionsIt.hasNext()) {
                        AuctionEntity i = closedAuctionsIt.next();
                        requestReplyObjectOutput.writeObject(i.getJSON().toString());
                    }
                    /*for ( bftProxyServer.BankEntity userBankEntity: usersBankEntities ) {

                        requestReplyObjectOutput.writeObject(userBankEntity.getJSONSecure().toString());
                    }*/

                    hasRequestReply = true;

                    break;

                case "LIST_ALL_BIDS_FROM_AUCTION":

                    auctionID = (Long) receivedRequestObjectInput.readObject();

                    if (this.bankRepo.findByAuctionID(auctionID).isPresent()) {

                        Iterator<BidEntity> bidsFromAuctionIt = BankServiceHelper.getBidsFromAuction(auctionID, bankRepo).iterator();

                        int numTotalBidsFromAuctionBankEntities = bankRepo.findByAuctionID(auctionID).get().getBids().size();

                        requestReplyObjectOutput.writeObject(numTotalBidsFromAuctionBankEntities);

                        while (bidsFromAuctionIt.hasNext()) {
                            BidEntity i = bidsFromAuctionIt.next();
                            requestReplyObjectOutput.writeObject(i.getJSON().toString());
                        }

                        hasRequestReply = true;

                    }

                    break;

                case "LIST_ALL_BIDS_FROM_USER":

                    who = (String) receivedRequestObjectInput.readObject();

                    if (this.bankRepo.findByUserName(who).isPresent()) {

                        Iterator<BidEntity> bidsFromUserIt = BankServiceHelper.getBidsFromUser(who, bankRepo).iterator();

                        int numTotalBidsFromUserBankEntities = bankRepo.findByUserName(who).get().getBids().size();

                        requestReplyObjectOutput.writeObject(numTotalBidsFromUserBankEntities);

                        while (bidsFromUserIt.hasNext()) {
                            BidEntity i = bidsFromUserIt.next();
                            requestReplyObjectOutput.writeObject(i.getJSON().toString());
                        }

                        hasRequestReply = true;

                    }

                    break;

                case "CHECK_WINNER_BID_CLOSED_AUCTION":

                    auctionID = (Long) receivedRequestObjectInput.readObject();

                    if ((this.bankRepo.findByAuctionID(auctionID).isPresent()) && (this.bankRepo.findByClosedAuctionID(auctionID).isPresent())) {

                        BidEntity winnerBidFromAuction = BankServiceHelper.getWinnerBidFromAuction(auctionID, bankRepo);

                        requestReplyObjectOutput.writeObject(winnerBidFromAuction);

                        hasRequestReply = true;

                    }

                    break;

                default:

                    logger.log(Level.WARNING, "Methodcall" + bankServiceRequestType);
                    logger.log(Level.WARNING, "Unsupported Unordered Operation!!!");
                    break;

            }

            if (hasRequestReply) {

                requestReplyObjectOutput.flush();
                requestReplyByteArrayOutputStream.flush();

                requestReply = requestReplyByteArrayOutputStream.toByteArray();

                try {
                    byte[] encoded = Files.readAllBytes(Paths.get("config/keys/privatekey" + this.id));
                    String key = new String(encoded);
                    PrivateKey privateKey = RSAKeyUtils.getPrivateKeyFromString(key);
//                    byte[] signature = TOMUtil.signMessage(privateKey, requestReply);
                    Signature sha_rsa = Signature.getInstance("SHA512withRSA");
                    sha_rsa.initSign(privateKey);
                    sha_rsa.update(requestReply);
                    byte[] signature = sha_rsa.sign();

                    finalReply = new byte[requestReply.length + 128];

                    System.arraycopy(requestReply, 0, finalReply, 0, requestReply.length);
                    System.arraycopy(signature, 0, finalReply, requestReply.length, 128);
                } catch (Exception exception) {

                    exception.printStackTrace();

                }

            } else {

                requestReply = new byte[0];

            }

        } catch (IOException | ClassNotFoundException executeOrderedException) {

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
                ) {

            snapshotObjectOutput.writeObject(this.bankRepo.version);

            return snapshotByteArrayOutputStream.toByteArray();

        } catch (IOException snapshotException) {

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

                ) {

            this.bankRepo.version = (int) snapshotObjectInput.readObject();

        } catch (IOException | ClassNotFoundException snapshotException) {

            logger.log(Level.SEVERE, "Error while installing Snapshot", snapshotException);

        }

    }

}
