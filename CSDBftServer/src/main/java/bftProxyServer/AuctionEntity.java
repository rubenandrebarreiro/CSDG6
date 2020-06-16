package bftProxyServer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class AuctionEntity implements Serializable {

	private Long id;
	
	private String ownerUsername;
	
	private boolean isClosed;
	
    private Map<Long, BidEntity> bids;

    protected AuctionEntity() {
    	
    	// Empty Constructor
    	
    }
    
    /*@SuppressWarnings("unchecked")
	protected bftProxyServer.AuctionEntity(String jsonObjectString)
    		  throws JsonMappingException, JsonProcessingException {
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	
		Map<String, Object> auctionEntityMap = 
    			objectMapper.readValue(jsonObjectString, Map.class);
    	
    	this.id = (Long) auctionEntityMap.get("id");
    	
    	this.ownerUsername = (String) auctionEntityMap.get("ownerUsername");
    	
    	this.state = (String) auctionEntityMap.get("state");
    	
    	this.bids = (HashMap<Long, bftProxyServer.BidEntity>) objectMapper.readValue("bids", HashMap.class);
    	
    }*/
    
    protected AuctionEntity(String id, String ownerUsername, String state, String bidsString) {
    	
    	this.id = Long.parseLong(id);
    	this.ownerUsername = ownerUsername;
    	this.isClosed = false;
    	
    	
    	String[] bidsFromAuctionParts = bidsString.split(" \\| ");
    	
    	this.bids = new HashMap<>();
    	
    	
    	for ( String bidFromAuction : bidsFromAuctionParts ) {
    		
    		BidEntity bidEntity = new BidEntity(bidFromAuction);
    		
    		this.bids.put(bidEntity.getID(), bidEntity);
    		
    	}
    	
    }

    public AuctionEntity(Long id, String ownerUsername) {
        this.id = id;
        this.ownerUsername = ownerUsername;
        this.isClosed = false;
        this.bids = new HashMap<>();
    }

    public Long getID() {

    	return this.id;
    
    }
    
    public String getOwnerUsername() {
    	
    	return this.ownerUsername;
    
    }
    
    public boolean isClosed() {
    	
    	return this.isClosed;
    	
    }
    
    public void close() {
    	
    	this.isClosed = true;
    	
    }

    public Map<Long, BidEntity> getBids() {
    
    	return this.bids;
    
    }
    
    public void addBid(String username, BidEntity bid) {
    	
    	this.bids.put(bid.getID(), bid);
    	
    }
    
	public Long getLastBidAmount() {
		if(this.getBids() == null || this.getBids().get(new Long( ( this.bids.size() - 1 ) ) )==null)
			return Long.parseLong("0");
		return this.getBids().get(new Long( ( this.bids.size() - 1 ) ) )
							 .getAmount();
		
	}
    
	public BidEntity getLastBid() {
		
		return this.getBids().get(new Long( ( this.bids.size() - 1 ) ) );
		
	}
	
	public boolean validBidAmount(Long amount) {
		
		return amount > this.getLastBidAmount();
		
	}
	
	public JSONObject getJSON(){
        return new JSONObject().put("id",getID()).put("ownerUsername",this.ownerUsername).put("isClosed",isClosed()).put("bids", getBids());
    }
	
}
