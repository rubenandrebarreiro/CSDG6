package smartContracts;

import java.io.Serializable;

public class CreateAuctionSmartContract implements SmartContract, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long creationTimestamp;
	
	private SecurityManager securityManager;
	
	
	public CreateAuctionSmartContract() {
		
	}
	
	public CreateAuctionSmartContract(Long id) {
		
		this.id = id;
		this.creationTimestamp = System.currentTimeMillis();
	
	}
	
	public Long getID() {
		return this.id;
	}
	
	public Long getCreationTimestamp() {
		return this.creationTimestamp;
	}
	
	public boolean isExpired() {
		
		// Each Smart Contract expires after 10 minutes
		return System.currentTimeMillis() > (this.creationTimestamp + 600000L);
		
	}
	
	@Override
	public void init() throws Exception {

		if(!this.isExpired()) {
			
			System.setProperty("java.security.policy", "file:/C:/Users/rubenandrebarreiro/git/CSDG6/CSDBftServer/policies");
		
			// Create a security manager
			this.securityManager = new SecurityManager();
		    
		    // Set the system security manager
		    System.setSecurityManager(this.securityManager);
		    
		}
		else {
			
			throw new Exception("This Smart Contract for the Creation of an Auction it's expired!!!");
		
		}
		
	}

	@Override
	public void run() throws Exception {
		
		if(!this.isExpired()) {
			
			if(this.securityManager != null) {
				
				//this.securityManager.checkAccept(arg0, arg1);
				
				try {
					
					this.securityManager.checkPackageAccess("bftProxyServer");
					
					System.out.println("This Smart Contract it's valid!!!");
					
				}
				catch(Exception packageException) {
					
					System.err.println("ACCESS DENIED to a Package!!!");
					
				}
				
			}
			else {
				
				throw new Exception("This Smart Contract for the Creation of an Auction isn't valid!!!");
				
			}
			
		}
		else {
			
			throw new Exception("This Smart Contract for the Creation of an Auction it's expired!!!");
		
		}

	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub

	}

}
