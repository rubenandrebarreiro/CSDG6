import src.SmartContract;

import java.util.*;

public class AuctionSmartContract implements SmartContract {

    private static volatile List<String> operations;
    private static boolean t;

    public void init() throws Exception {
        System.out.println("Init");
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
        System.out.println("Created money opeartion");
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
