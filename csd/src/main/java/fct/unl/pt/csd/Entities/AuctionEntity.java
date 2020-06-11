package fct.unl.pt.csd.Entities;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.CollectionTable;
import javax.persistence.Entity;

@Entity
public class AuctionEntity {

	@Id
	@JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@JsonProperty("ownerUsername")
    private String ownerUsername;
	
	@JsonProperty("state")
    private String state;
	
	@CollectionTable
	@JsonProperty("bids")
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
	
	public String toJSONString() throws JsonProcessingException {
		
		return new ObjectMapper().writeValueAsString(this);
		
	}
	
}
