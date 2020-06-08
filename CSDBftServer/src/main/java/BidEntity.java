import java.io.Serializable;

public class BidEntity implements Serializable {

	private Long id;
    
	private String username;
	
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
	
	public String toString() {
		
		return "bid#" + this.id + 
			   "[" + 
				  "username: " +  this.username +
				  "amount: " + this.amount +
			   "]";
		
	}
    
}
