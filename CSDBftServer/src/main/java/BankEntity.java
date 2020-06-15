import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BankEntity implements Serializable {
    private Long id;
    private String userName, password;
    private Long amount;
    private Map<Long, AuctionEntity> auctions;
    private Map<Long, BidEntity> bids;
    private String roles;

    public BankEntity(String userName, String password, Long amount, String[] roles){
        this.userName = userName;
        this.password = password;
        this.amount = amount;
        this.auctions = new HashMap<>();
        this.bids = new HashMap<>();
        this.roles = "ROLE_USER";
        for(int i = 0; i < roles.length;i++){
            if(!roles[i].equalsIgnoreCase("ROLE_USER"))
                this.roles += "@/&@"+roles[i];
        }
    }

    public String getStringRoles(){
        return this.roles;
    }

    public ArrayList<String> getRoles(){
        String[] splitter = roles.split("@/&@");
        if(splitter.length <= 0)
            return null;
        return (ArrayList<String>) Arrays.asList(splitter);
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
        return new JSONObject().put("username",getOwnerName()).put("password",this.password).put("amount",getAmount()).put("roles", this.roles);
    }

    public JSONObject getJSONSecure(){
        return new JSONObject().put("username",getOwnerName()).put("amount",getAmount());
    }
}
