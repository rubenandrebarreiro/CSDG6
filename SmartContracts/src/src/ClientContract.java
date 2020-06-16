public class ClientContract extends AuctionSmartContract {

    public ClientContract(String who1) {
        super(who1);
    }

    public void run() {
//        createMoney(100);
//        createAuction();
//        bid(Long.parseLong("100"),Long.parseLong("0"));
//        System.out.println("YO?");
            System.out.println("yoyoyo");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Miss you");
    }
}
