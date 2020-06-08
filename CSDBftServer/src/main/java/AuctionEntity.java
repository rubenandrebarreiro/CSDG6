import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AuctionEntity implements Serializable {

	private Long id;
	
	private String ownerUsername;
	
	private String state;
	
    private Map<Long, BidEntity> bids;

    protected AuctionEntity() {
    	
    	// Empty Constructor
    	
    }
    
    /*@SuppressWarnings("unchecked")
	protected AuctionEntity(String jsonObjectString)
    		  throws JsonMappingException, JsonProcessingException {
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	
		Map<String, Object> auctionEntityMap = 
    			objectMapper.readValue(jsonObjectString, Map.class);
    	
    	this.id = (Long) auctionEntityMap.get("id");
    	
    	this.ownerUsername = (String) auctionEntityMap.get("ownerUsername");
    	
    	this.state = (String) auctionEntityMap.get("state");
    	
    	this.bids = (HashMap<Long, BidEntity>) objectMapper.readValue("bids", HashMap.class);
    	
    }*/
    
    protected AuctionEntity(String id, String ownerUsername, String state, String bidsString) {
    	
    	this.id = Long.parseLong(id);
    	this.ownerUsername = ownerUsername;
    	this.state = state;
    	
    	
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
        this.state = "opened";
        this.bids = new HashMap<>();
    }

    public Long getID() {

    	return this.id;
    
    }
    
    public String getOwnerUsername() {
    	
    	return this.ownerUsername;
    
    }
    
    public String getState() {
    	
    	return this.state;
    	
    }
    
    public void close() {
    	
    	this.state = "closed";
    	
    }

    public Map<Long, BidEntity> getBids() {
    
    	return this.bids;
    
    }
    
    public void addBid(String username, Long amount) {
    	
    	BidEntity bid = new BidEntity( new Long( this.bids.size() ),
    								   username, amount);
    	
    	this.bids.put(bid.getID(), bid);
    	
    }
    
	public Long getLastBidAmount() {
		
		return this.getBids().get(new Long( ( this.bids.size() - 1 ) ) )
							 .getAmount();
		
	}
    
	public boolean validBidAmount(Long amount) {
		
		return amount > this.getLastBidAmount();
		
	}
	
}
