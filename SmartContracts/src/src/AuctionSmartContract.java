import src.SmartContract;

import java.io.FilePermission;
import java.util.*;

public class AuctionSmartContract implements SmartContract {

    private static volatile List<String> operations= new ArrayList<>();
    private static boolean t;
    private static String who;
    private static SecurityManager sm;

    public AuctionSmartContract(String who1){
        who=who1;
//        SecurityManager s = new SecurityManager();
//        System.setSecurityManager(s);
        // set the policy file as the system securuty policy
        System.setProperty("java.security.policy", "file:C:\\Users\\35196\\Desktop\\Faculdade\\CSD\\CSDG6\\CSDBftServer\\src\\main\\java\\smartContracts\\grants\\our.policy");

        // create a security manager
        sm = new SecurityManager();

        // set the system security manager
        System.setSecurityManager(sm);
    }

    public final void init() throws Exception {
        sm.checkPermission(new FilePermission("*", "read,write"));
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

}
