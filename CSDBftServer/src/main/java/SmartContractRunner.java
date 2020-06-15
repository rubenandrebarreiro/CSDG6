import bftsmart.tom.MessageContext;
import src.SmartContract;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;

class MappableContract {
    String who;
    SmartContract contract;
    MessageContext messageContext;

    MappableContract(String who, SmartContract s, MessageContext messageContext) {
        this.who = who;
        this.contract = s;
        this.messageContext = messageContext;
    }
}

public class SmartContractRunner implements Serializable, Runnable {
    private Map<Integer, MappableContract> contracts;
    int i;
    private BankService bS;
    Logger logger;

    public SmartContractRunner(BankService bS) {
        this.contracts = new ConcurrentHashMap<>();
        this.i = 0;
        this.bS = bS;
        logger = Logger.getLogger(SmartContractRunner.class.getName());
    }

    public int runContract(SmartContract s, String who, MessageContext m) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(s);
        contracts.put(i, new MappableContract(who, s, m));

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

        //executor.shutdown();
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
        while (true) {
            for (Integer i : contracts.keySet()) {
                ops = (ArrayList<String>) contracts.get(i).contract.getOperations();
                if (ops != null)
                    for (String s : ops) {
                        System.out.println(s);
                        contracts.get(i).contract.clearOperations();
                        ss = s.split(" ");
                        switch (ss[0]) {
                            case "CREATE_MONEY":
                                bS.logger.info("Created Money");
                                bS.logger.info(contracts.get(i).who + " had " + bS.bankRepo.findByUserName(contracts.get(i).who).get().getAmount() + " now has " + (bS.bankRepo.findByUserName(contracts.get(i).who).get().getAmount() + Long.parseLong(ss[1])));
                                BankServiceHelper.createMoney(contracts.get(i).who, bS.bankRepo.findByUserName(contracts.get(i).who).get().getAmount() + Long.parseLong(ss[1]), bS.bankRepo);
                                bS.bankRepo.save(bS.id);
                                //bS.appExecuteOrdered(createMoney(contracts.get(i).who,bS.bankRepo.findByUserName(contracts.get(i).who).get().getAmount(),Long.parseLong(ss[1])), contracts.get(i).messageContext);
                                break;
                            case "TRANSFER_MONEY":
                                bS.logger.info("TRANSFER Money");
                                if (bS.bankRepo.findByUserName(contracts.get(i).who).get().getAmount() - Long.parseLong(ss[2]) >= 0) {
                                    BankServiceHelper.transferMoney(contracts.get(i).who, bS.bankRepo.findByUserName(contracts.get(i).who).get().getAmount() - Long.parseLong(ss[2]), ss[1], Long.parseLong(ss[2]) + bS.bankRepo.findByUserName(ss[1]).get().getAmount(), bS.bankRepo);
                                    bS.bankRepo.save(bS.id);
                                }
                                break;
                            case "CREATE_AUCTION":
                                if (bS.bankRepo.findByUserName(contracts.get(i).who).get().getRoles().contains("ROLE_AUCTION_MAKER"))
                                    BankServiceHelper.createAuction(Long.parseLong(bS.bankRepo.getNumAuctions() + ""), contracts.get(i).who, bS.bankRepo);
                                break;
                            case "BID":
                                if(!contracts.get(i).who.equalsIgnoreCase(contracts.get(ss[2]).who))
                                    BankServiceHelper.bid(new BidEntity(Long.parseLong(ss[2]),contracts.get(i).who,Long.parseLong(ss[1])), bS.bankRepo);
                        }
                    }

            }
        }
    }
}

