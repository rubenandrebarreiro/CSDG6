import src.SmartContract;

import java.util.*;

public class AuctionSmartContract implements SmartContract {

    private static volatile List<String> operations= new ArrayList<>();
    private static boolean t;
    private static String who;

    public AuctionSmartContract(String who1){
        who=who1;
//        SecurityManager s = new SecurityManager();
//        System.setSecurityManager(s);
    }

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
        operations.add("CREATE_MONEY "+amount);
    }

    public final void tranferMoney(String to,int amount) {
        if(operations == null)
            operations = new ArrayList<>();
        operations.add("TRANSFER_MONEY " + to + " " +amount);
    }

    public final void createAuction(){
        if(operations == null)
            operations = new ArrayList<>();
        operations.add("CREATE_AUCTION ");
    }

    public final void bid(Long amount, Long id){
        if(operations == null)
            operations = new ArrayList<>();
        operations.add("BID "+amount + " " + id);
    }

    @Override
    public List<String> getOperations() {
        return operations;
    }

    public void clearOperations(){
        operations = new ArrayList<>();
    }

    @Override
    public final String getOwner() {
        return who;
    }

    @Override
    public Object clone() {
        try {
            return (AuctionSmartContract) super.clone();
        } catch (CloneNotSupportedException e) {
            return new AuctionSmartContract(this.who);
        }
    }

}
