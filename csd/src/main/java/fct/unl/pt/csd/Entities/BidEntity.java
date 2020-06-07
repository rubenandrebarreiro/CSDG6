package fct.unl.pt.csd.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    
    protected BidEntity() {
    
    	// Empty Constructor
    	
    }
    
    public BidEntity(Long id, String username, Long amount) {
    	
        this.id = id;
        this.username = username;
        this.amount = amount;
    
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
    
}
