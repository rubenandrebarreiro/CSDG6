package fct.unl.pt.csd.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
public class BidEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
	private Long id;
    
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("amount")
    private Long amount;
	
	@JsonProperty("timestamp")
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
    									 .split("\\]")[0].split("amount: ")[1]
				
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
    
	public String toJSONString() throws JsonProcessingException {
		
		return new ObjectMapper().writeValueAsString(this);
		
	}
	
	public String toString() {
		
		return "bid#" + this.id + 
			   "[" + 
				  "username: " +  this.username +
				  "amount: " + this.amount +
			   "]";
		
	}
    
	public JSONObject getJSON(){
        return new JSONObject().put("id",getID()).put("username",this.username).put("amount",getAmount()).put("timestamp",getTimestamp());
    }
   
}
