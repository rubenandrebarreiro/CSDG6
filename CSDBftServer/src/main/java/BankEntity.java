import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BankEntity implements Serializable {
    private Long id;
    private String userName, password;
    private Long amount;
    private Map<Long, AuctionEntity> auctions;
    private Map<Long, BidEntity> bids;

    public BankEntity(String userName, String password, Long amount){
        this.userName = userName;
        this.password = password;
        this.amount = amount;
        this.auctions = new HashMap<>();
        this.bids = new HashMap<>(); 
    }

    public Long getID(){
        return id;
    }

    public String getOwnerName(){
        return userName;
    }

    public String getPassword(){return password;}

    public Long getAmount(){
        return amount;
    }

    public void updateAmount(long amount){
        this.amount = amount;
    }

    public void addAuction(AuctionEntity auction) {
    	this.auctions.put(auction.getID(), auction);
    }
    
    public void makeBidForAuction(AuctionEntity auction, BidEntity bid) {
    	auction.addBid(this.userName, bid);
    	this.bids.put(bid.getID(), bid);
    }
    
    public Map<Long, BidEntity> getBids() {
    	return this.bids;
    }
    
    public JSONObject getJSON(){
        return new JSONObject().put("username",getOwnerName()).put("password",this.password).put("amount",getAmount());
    }

    public JSONObject getJSONSecure(){
        return new JSONObject().put("username",getOwnerName()).put("amount",getAmount());
    }
}
