package bftProxyServer;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.*;

public class BankEntity implements Serializable {
    private Long id;
    private String userName, password;
    private Long amount;
    private Map<Long, AuctionEntity> auctions;
    private Map<Long, BidEntity> bids;
    private String roles;

    public BankEntity(String userName, String password, Long amount, String[] roles) {
        this.userName = userName;
        this.password = password;
        this.amount = amount;
        this.auctions = new HashMap<>();
        this.bids = new HashMap<>();
        this.roles = "ROLE_USER";
        if (roles != null)
            for (int i = 0; i < roles.length; i++) {
                if (!roles[i].equalsIgnoreCase("ROLE_USER"))
                    this.roles += "@/&@" + roles[i];
            }
        else
            this.roles += "@/&@";
    }

    public String getStringRoles() {
        return this.roles;
    }

    public List<String> getRoles() {
        String[] splitter = roles.split("@/&@");
        if (splitter.length <= 0)
            return null;
        return Arrays.asList(splitter);
    }

    public Long getID() {
        return id;
    }

    public String getOwnerName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Long getAmount() {
        return amount;
    }

    public void updateAmount(long amount) {
        this.amount = amount;
    }

    public void addAuction(AuctionEntity auction) {
        this.auctions.put(auction.getID(), auction);
    }

    public void makeBidForAuction(AuctionEntity auction, BidEntity bid) {
        auction.addBid(this.userName, bid);
        this.bids.put(bid.getID(), bid);
        // Freeze money
        this.amount -= bid.getAmount();
    }

    public Map<Long, BidEntity> getBids() {
        return this.bids;
    }

    public JSONObject getJSON() {
        return new JSONObject().put("username", getOwnerName()).put("password", this.password).put("amount", getAmount()).put("roles", this.roles);
    }

    public JSONObject getJSONSecure() {
        return new JSONObject().put("username", getOwnerName()).put("amount", getAmount());
    }
}
