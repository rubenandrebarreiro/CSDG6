package bftProxyServer;

import java.io.Serializable;

import org.json.JSONObject;

public class BidEntity implements Serializable {

	private Long id;
    
	private String username;
	
	private Long amount;
	
	private Long timestamp;
    
    protected BidEntity() {
    
    	// Empty Constructor
    	
    }
    
    public BidEntity(Long id, String username, Long amount) {
    	
        this.id = id;
        this.username = username;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
    
    }
    
    public BidEntity(Long id, String username, Long amount, Long timestamp) {
    	
        this.id = id;
        this.username = username;
        this.amount = amount;
        this.timestamp = timestamp;
    
    }
    
    public BidEntity(String bidString) {
    	
    	this.id = Long.parseLong(bidString.split("\\[")[0].split("#")[1]);
    	
    	this.username = bidString.split("\\[")[1].split(" , ")[0].split("username: ")[1];
    	
    	this.amount = 
    			Long.parseLong
    					(
    			
    							bidString.split("\\[")[1].split(" , ")[1]
    									 .split("amount: ")[1]
				
    					);
	
    	this.timestamp = Long.parseLong
				(
		    			
						bidString.split("\\[")[1].split(" , ")[1]
								 .split("\\]")[0].split("timestamp: ")[1]
		
				);
    }

    public Long getID() {

    	return this.id;
    
    }

    public String getUsername() {
    
    	return this.username;
    
    }
    
    public Long getAmount() {
    
    	return this.amount;
    
    }
	
    public Long getTimestamp() {
    	return this.timestamp;
    }
    
	public String toString() {
		
		return "bid#" + this.id + 
			   "[" + 
				  "username: " +  this.username + " , " +
				  "amount: " + this.amount + " , " +
				  "timestamp: " + this.timestamp +
			   "]";
		
	}
	
	public JSONObject getJSON(){
        return new JSONObject().put("id",getID()).put("username",this.username).put("amount",getAmount()).put("timestamp",getTimestamp());
    }
    
}
