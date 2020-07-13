package bftProxyServer;

import org.json.JSONObject;
import src.SmartContract;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class SmartContractRunner implements Serializable, Runnable {
//    private volatile Map<Integer, MappableContract> contracts;
    private volatile Map<Integer, SmartContract> contracts;
    private volatile int i;
    private BankService bS;
    Logger logger;

    public SmartContractRunner(BankService bS) {
        this.contracts = new ConcurrentHashMap<>();
        this.i = 0;
        this.bS = bS;
        logger = Logger.getLogger(SmartContractRunner.class.getName());
    }

    public int runContract(SmartContract s, String who) {
        if(s.getOwner()!=who)
            return -1;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(s);
//        contracts.put(i, new MappableContract(s,future));
        contracts.put(i, s);
/*

        try {
            future.get(SmartContract.MAX_EXECUTION_TIME, TimeUnit.MILLISECONDS);
            System.out.println("Smart Contract's Execution completed successfully!!!");

        } catch (InterruptedException interruptedException) {

            System.out.println("Interruption Exception!!! Cancelling the runnable Smart Contract...");
            future.cancel(true);

        } catch (ExecutionException executionException) {

            System.out.println("Execution Exception!! Cancelling the runnable Smart Contract...");
            System.out.println(executionException.getCause());
            System.out.println(executionException.getMessage());
            future.cancel(true);

        } catch (TimeoutException timeoutException) {

            System.out.println("Time Out Exception!!! Cancelling the runnable Smart Contract...");

            future.cancel(true);

        }
*/

        executor.shutdown();
        return i++;
    }

    public byte[] createMoney(String who, Long initialAmount, Long amount) {
        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("CREATE_MONEY");
            requestToSendObjectOutput.writeObject(who);
            requestToSendObjectOutput.writeLong(initialAmount);
            requestToSendObjectOutput.writeLong(initialAmount + amount);
            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            return requestToSendByteArrayOutputStream.toByteArray();

        } catch (IOException e) {

            System.out.println("Exception in creation of Money by User/Client: " +
                    e.getMessage());

        }
        return null;
    }

    @Override
    public void run() {
        ArrayList<String> ops;
        String[] ss = new String[0];
        JSONObject j = new JSONObject();
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Integer i : contracts.keySet()) {
                ops = (ArrayList<String>) contracts.get(i).getOperations();
                if (ops != null){

                    for (String s : ops) {
                        contracts.get(i).clearOperations();
                        ss = s.split(" ");
                        String www = contracts.get(i).getOwner();
                        switch (ss[0]) {
                            case "CREATE_MONEY":
                                bS.logger.info("Created Money");
                                bS.logger.info(www + " had " + bS.bankRepo.findByUserName(www).get().getAmount() + " now has " + (bS.bankRepo.findByUserName(www).get().getAmount() + Long.parseLong(ss[1])));
                                j=BankServiceHelper.createMoney(www, bS.bankRepo.findByUserName(www).get().getAmount() + Long.parseLong(ss[1]), bS.bankRepo);

                                //bS.appExecuteOrdered(createMoney(contracts.get(i).who,bS.bankRepo.findByUserName(contracts.get(i).who).get().getAmount(),Long.parseLong(ss[1])), contracts.get(i).messageContext);
                                break;
                            case "TRANSFER_MONEY":
                                bS.logger.info("TRANSFER Money");
                                bS.logger.info(""+bS.bankRepo.findByUserName(www).get().getAmount());
                                bS.logger.info(""+Long.parseLong(ss[2]));
                                if (bS.bankRepo.findByUserName(www).get().getAmount() - Long.parseLong(ss[2]) >= 0) {
                                    j=BankServiceHelper.transferMoney(www, bS.bankRepo.findByUserName(www).get().getAmount() - Long.parseLong(ss[2]), ss[1], Long.parseLong(ss[2]) + bS.bankRepo.findByUserName(ss[1]).get().getAmount(), bS.bankRepo);

                                }
                                break;
                            case "CREATE_AUCTION":
                                bS.logger.info("Created Auction");
                                if (bS.bankRepo.findByUserName(www).get().getRoles().contains("ROLE_AUCTION_MAKER"))
                                    j=BankServiceHelper.createAuction(Long.parseLong(i + ""),www, bS.bankRepo);
                                break;
                            case "BID":
                                if(bS.bankRepo.findByAuctionID(Long.parseLong(ss[2])).isPresent())
                                    if(!www.equalsIgnoreCase(bS.bankRepo.findByAuctionID(Long.parseLong(ss[2])).get().getOwnerUsername()))
    //                                   j= bftProxyServer.BankServiceHelper.bid(new bftProxyServer.BidEntity(Long.parseLong(ss[2]),www,Long.parseLong(ss[1])), bS.bankRepo);
                                        j = BankServiceHelper.createBid(Long.parseLong(ss[2]),Long.parseLong(ss[2]),Long.parseLong(ss[1]),www,bS.bankRepo);
                                    break;
                            case "CLOSE_AUCTION":
                                    if(www.equalsIgnoreCase(contracts.get(Integer.parseInt(ss[2])).getOwner()) || bS.bankRepo.findByUserName(www).get().getRoles().contains("ROLE_AUCTION_MANAGER")){
                                        j = BankServiceHelper.closeAution(Long.parseLong(ss[1]),www,bS.bankRepo);
//                                        contracts.get(Integer.parseInt(ss[2])).future.cancel(true);
                                    }
                                break;
                            case "TERMINATE":
//                                if(contracts.get(Integer.parseInt(ss[2])).getOwner().equals(www))
//                                    contracts.get(Integer.parseInt(ss[2])).future.cancel(true);
                                break;
                        }
                        if(!j.has("error"))bS.bankRepo.save(bS.id);
                    }
                }
            }
        }
    }
}

