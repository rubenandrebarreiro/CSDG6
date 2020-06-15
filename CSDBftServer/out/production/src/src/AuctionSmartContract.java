import src.SmartContract;

import java.util.*;

public class AuctionSmartContract implements SmartContract {

    private static List<String> operations;
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
        operations.add("CREATE_MONEY "+amount);
    }

    @Override
    public List<String> getOperations() {
        List<String> temp = operations;
        operations = new ArrayList<>();
        return temp;
    }

}
