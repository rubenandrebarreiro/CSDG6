import src.SmartContract;

import java.util.*;

public class AuctionSmartContract implements SmartContract {

    private static volatile List<String> operations;
    private static boolean t;

    public final void init() throws Exception {

    }

    public void run() {
        System.out.println("Run");
    }

    public final void terminate() {
        t=false;
//        System.out.println("Terminate");
    }

    @Override
    public final void createMoney(int amount) {
        if(operations == null)
            operations = new ArrayList<>();
        boolean flag = operations.add("CREATE_MONEY "+amount);
        System.out.println("Created money operation");
        System.out.println(flag);
    }

    public final void tranferMoney(String to,int amount) {
        if(operations == null)
            operations = new ArrayList<>();
        boolean flag = operations.add("TRANSFER_MONEY " + to + " " +amount);
        System.out.println("Created transfer operation");
        System.out.println(flag);
    }

    public final void createAuction(){
        if(operations == null)
            operations = new ArrayList<>();
        boolean flag = operations.add("CREATE_AUCTION ");
        System.out.println("Created auction operation");
        System.out.println(flag);
    }

    public final void bid(Long amount, Long id){
        if(operations == null)
            operations = new ArrayList<>();
        boolean flag = operations.add("BID "+amount + " " + id);
        System.out.println("Created bid operation");
        System.out.println(flag);
    }

    @Override
    public List<String> getOperations() {
        return operations;
    }

    public void clearOperations(){
        operations = new ArrayList<>();
    }

}
