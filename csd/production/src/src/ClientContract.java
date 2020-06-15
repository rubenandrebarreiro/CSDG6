package src;

public class ClientContract extends AuctionSmartContract {

    public void run() {
        createAuction();
        while(t)
        System.out.println("This was runned by the client");
    }
}
