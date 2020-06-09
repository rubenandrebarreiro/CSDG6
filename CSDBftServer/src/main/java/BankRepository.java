import javax.swing.text.html.Option;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class BankRepository implements Serializable {

	private Map<String, BankEntity> users;
	private Map<Long, AuctionEntity> auctions;
	private Map<Long, AuctionEntity> openedAuctions;
	private Map<Long, AuctionEntity> closedAuctions;
	private Map<Long, BidEntity> bids;
    
    public int version;

    public BankRepository() {
        this.users = new HashMap<String, BankEntity>();
        this.auctions = new HashMap<Long, AuctionEntity>();
        this.openedAuctions = new HashMap<Long, AuctionEntity>();
        this.closedAuctions = new HashMap<Long, AuctionEntity>();
        this.bids = new HashMap<Long, BidEntity>();
        this.version=0;
    }

    public Optional<BankEntity> findByUserName(String userName) {
        BankEntity b = users.get(userName);
        return b == null? Optional.empty() : Optional.of(users.get(userName));
    }
    
    public Optional<AuctionEntity> findByAuctionID(Long id) {
    	AuctionEntity a = auctions.get(id);
        return a == null? Optional.empty() : Optional.of(auctions.get(id));
    }
    
    public Optional<AuctionEntity> findByOpenedAuctionID(Long id) {
    	AuctionEntity a = openedAuctions.get(id);
        return a == null? Optional.empty() : Optional.of(openedAuctions.get(id));
    }
    
    public Optional<AuctionEntity> findByClosedAuctionID(Long id) {
    	AuctionEntity a = closedAuctions.get(id);
        return a == null? Optional.empty() : Optional.of(closedAuctions.get(id));
    }

    public Optional<BidEntity> findByBidID(Long id) {
    	BidEntity b = bids.get(id);
        return b == null? Optional.empty() : Optional.of(bids.get(id));
    }
    
    public BankEntity newUserBankEntity(BankEntity b) {
        users.put(b.getOwnerName(),b);
        return users.get(b.getOwnerName());
    }

    public AuctionEntity createAuction(AuctionEntity a) {
    	auctions.put(a.getID(),a);
        openedAuctions.put(a.getID(),a);
        return auctions.get(a.getID());
    }
    
    public AuctionEntity closeAuction(AuctionEntity a) {
        auctions.put(a.getID(), a);
    	openedAuctions.remove(a.getID());
    	closedAuctions.put(a.getID(), a);
    	return auctions.get(a.getID());
    }
    
    public BidEntity createBid(BidEntity bid) {
        bids.put(bid.getID(),bid);
        return bids.get(bid.getID());
    }
    
    
    
    // Function to get the Spliterator
    public static<T> Iterable<T> iteratorToIterable(Iterator<T> iterator) {
        return () -> iterator;
    }

    public Iterable<BankEntity> findAll(){
        return iteratorToIterable(this.users.values().iterator());
    }

    public Iterable<AuctionEntity> findAllOpenedAuctions(){
        return iteratorToIterable(this.openedAuctions.values().iterator());
    }

    public Iterable<AuctionEntity> findAllClosedAuctions(){
        return iteratorToIterable(this.closedAuctions.values().iterator());
    }

    public Iterable<BidEntity> findAllBidsFromAuction(Long id){
        return iteratorToIterable(this.auctions.get(id).getBids().values().iterator());
    }

    public Iterable<BidEntity> findAllBidsFromUser(String who){
        return iteratorToIterable(this.users.get(who).getBids().values().iterator());
    }
    
    public BidEntity checkWinnerBidFromAuction(Long id){
        return this.auctions.get(id).getLastBid();
    }
    
    public Iterator<BankEntity> usersIterator(){
        return this.users.values().iterator();
    }
    
    public Iterator<AuctionEntity> auctionsIterator(){
        return this.auctions.values().iterator();
    }
    
    public Iterator<AuctionEntity> openedAuctionsIterator(){
        return this.openedAuctions.values().iterator();
    }
    
    public Iterator<AuctionEntity> closedAuctionsIterator(){
        return this.closedAuctions.values().iterator();
    }

    public int getNumUsers(){
        return this.users.size();
    }

    public int getNumAuctions(){
        return this.auctions.size();
    }

    public int getNumOpenedAuctions(){
        return this.openedAuctions.size();
    }

    public int getNumClosedAuctions(){
        return this.closedAuctions.size();
    }

    protected void save(int id){
        this.version ++;
        try {
            File yourFile = new File("/users"+id+".ser");
            yourFile.createNewFile();
            FileOutputStream fileOut =
                    new FileOutputStream("/users"+id+".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.write(version);
            out.writeObject(users);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in /users"+id+".ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    protected void load(int id){
        try {
            FileInputStream fileIn = new FileInputStream("/users"+id+".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.version = in.readInt();
            this.users = (HashMap<String,BankEntity>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            return;
        }
    }
}
