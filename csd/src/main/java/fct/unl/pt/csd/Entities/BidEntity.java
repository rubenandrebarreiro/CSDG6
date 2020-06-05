package fct.unl.pt.csd.Entities;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
    
    protected BidEntity(String jsonObjectString)
  		  throws JsonMappingException, JsonProcessingException {
  	
	  	ObjectMapper objectMapper = new ObjectMapper();
	  	
	  	@SuppressWarnings("unchecked")
			Map<String, Object> auctionEntityMap = 
	  			objectMapper.readValue(jsonObjectString, Map.class);
	  	
	  	this.id = (Long) auctionEntityMap.get("id");
	  	
	  	this.username = (String) auctionEntityMap.get("username");
	  	
	  	this.amount = (Long) auctionEntityMap.get("amount");
  	
    }

    public BidEntity(Long id, String username, Long amount) {
    	
        this.id = id;
        this.username = username;
        this.amount = amount;
    
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
