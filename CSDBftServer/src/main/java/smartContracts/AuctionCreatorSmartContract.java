package smartContracts;

import java.io.FilePermission;
import java.io.Serializable;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.util.Enumeration;

import smartContracts.verifiers.bytecode.AuctionCreatorSmartContractByteCodeVerifier;
import smartContracts.verifiers.bytecode.ByteCodeVerifier;
import smartContracts.verifiers.bytecode.SmartContractByteCodeVerifier;

public class AuctionCreatorSmartContract implements SmartContract, Serializable {

	private static final long serialVersionUID = 1L;

	private static final Long DURABILITY = 30_000L;
	
	
	private Long auctionId;
	
	private String username;
	
	private Long creationTimestamp;
	
	private SecurityManager securityManager;
	
	private PermissionCollection permissionCollection;
	
	
	
	public AuctionCreatorSmartContract(Long id, String username, String classFolder) {
		
		this.auctionId = id;
		this.username = username;
		
		this.creationTimestamp = System.currentTimeMillis();
		
		this.permissionCollection = new Permissions();
		
		
		//Permission permission = new FilePermission(arg0, arg1);
		
//		this.permissionCollection.implies(permission);
		
	}
	
	
	public Long getAuctionID() {
	
		return this.auctionId;
	
	}

	public String getUsername() {
	
		return this.username;
	
	}
	
	public Long getCreationTimestamp() {
	
		return this.creationTimestamp;
	
	}
	
	public boolean isExpired() {
		
		// Each Smart Contract expires after 30 minutes
		return System.currentTimeMillis() > ( this.creationTimestamp + DURABILITY );
		
	}

	
	@Override
	public void init() throws Exception {
		
		if ( !this.isExpired() ) {
		
			SmartContractByteCodeVerifier.verifyByteCode(this.getClass(), (byte) 0);
			
		}
		else {
			
			throw new Exception("This Smart Contract for the Creation of an Auction it's expired!!!");
		
		}
		
	}
	
	@Override
	public void run() {
		
		if(!this.isExpired()) {
	
			
			
		}
		else {
			
			System.err.println("This Smart Contract for the Creation of an Auction it's expired!!!");
		
		}
		
	}

	
	@Override
	public void terminate() {
	
		// TODO Auto-generated method stub
		
		
	}
	
}
