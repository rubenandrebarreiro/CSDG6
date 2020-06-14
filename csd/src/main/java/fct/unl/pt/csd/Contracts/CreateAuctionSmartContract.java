package fct.unl.pt.csd.Contracts;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Hashtable;

public class CreateAuctionSmartContract implements SmartContract, Serializable, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long auctionId;
	
	private String username;
	
	private File createAuctionSmartContractsFile;
	
	private Long creationTimestamp;
	
	private SecurityManager securityManager;
	
	public CreateAuctionSmartContract() {
		
	}
	
	public CreateAuctionSmartContract(Long id, String username) {
		
		this.auctionId = id;
		this.username = username;
		
		this.creationTimestamp = System.currentTimeMillis();
	
		AccessController.doPrivileged(
				
				new PrivilegedAction<SmartContract>() {
				
					public SmartContract run() {
						
						String path = 
								System.getProperty("smart-contracts") +
								File.separator + 
								".auctions";
	 
						createAuctionSmartContractsFile = new File(path);
						
						return null;
						
					}
				
				}
			
			);
		
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
		
		// Each Smart Contract expires after 10 minutes
		return System.currentTimeMillis() > (this.creationTimestamp + 600000L);
		
	}
	
	@Override
	public void init() throws Exception {

		if(!this.isExpired()) {
			
			System.setProperty("java.security.policy", "TODOPATH");
		
			// Create a security manager
			this.securityManager = new SecurityManager();
		    
		    // Set the system security manager
		    System.setSecurityManager(this.securityManager);
		    
		    
		    if (this.securityManager != null) {
		    	
		    	this.securityManager.checkPermission(new CreateAuctionSmartContractPermission(String.valueOf(this.auctionId)));
		    
		    }
		 
		    // need a doPrivileged block to manipulate the file
		    try {
		    
		    	AccessController.doPrivileged(new PrivilegedExceptionAction<CreateAuctionSmartContract>() {
		        
		    		@SuppressWarnings("unchecked")
					public CreateAuctionSmartContract run() throws IOException {
			            
		    			Hashtable<Long, CreateAuctionSmartContract> createAuctionSmartContracts = null;
		    			
			            // try to open the existing file. Should have a locking
			            // protocol (could use File.createNewFile).
			            try {
			            	
			            	FileInputStream fis = new FileInputStream(createAuctionSmartContractsFile);
			            	
			            	ObjectInputStream ois = new ObjectInputStream(fis);
			            	
			            	createAuctionSmartContracts = (Hashtable<Long, CreateAuctionSmartContract>) ois.readObject();
			            	
			            	ois.close();
			            	
			            }
			            catch (Exception e) {
			            	// ignore, try and create new file
			            }
		 
		            // if scores is null, create a new hashtable
		            if (createAuctionSmartContracts == null)
		            	createAuctionSmartContracts = new Hashtable<Long, CreateAuctionSmartContract>();
		 
		            // update the score and save out the new high score
		            createAuctionSmartContracts.put(auctionId, new CreateAuctionSmartContract(auctionId, username));
		            FileOutputStream fos = new FileOutputStream(createAuctionSmartContractsFile);
		            ObjectOutputStream oos = new ObjectOutputStream(fos);
		            oos.writeObject(createAuctionSmartContracts);
		            oos.close();
		            
		            return null;
		        }
		        });
		    }
		    catch (PrivilegedActionException pae) {
		        throw (IOException) pae.getException();
		    }
		    
		    
		    
		    
		}
		else {
			
			throw new Exception("This Smart Contract for the Creation of an Auction it's expired!!!");
		
		}
		
	}
	
	@Override
	public void run() {
		
		if(!this.isExpired()) {
	
			System.setProperty("java.security.policy", "TODOPATH");
			
			// Create a security manager
			this.securityManager = new SecurityManager();
		    
		    // Set the system security manager
		    System.setSecurityManager(this.securityManager);
		    
		    
		    if (this.securityManager != null) {
		    	
		    	this.securityManager.checkPermission(new CreateAuctionSmartContractPermission(String.valueOf(this.auctionId)));
		    
		    }
		 
		    CreateAuctionSmartContract createAuctionSmartContract = null;
		 
		    // need a doPrivileged block to manipulate the file
		    try {
		    	createAuctionSmartContract = 
		    			(CreateAuctionSmartContract) AccessController.doPrivileged(
		    					new PrivilegedExceptionAction<CreateAuctionSmartContract>() {
								        public CreateAuctionSmartContract run() 
								            throws IOException, ClassNotFoundException {
								        	
									            Hashtable<Long, CreateAuctionSmartContract> createAuctionSmartContracts = null;
									            
									            // try to open the existing file. Should have a locking
									            // protocol (could use File.createNewFile).
									            FileInputStream fis = new FileInputStream(createAuctionSmartContractsFile);
									            
									            ObjectInputStream ois = new ObjectInputStream(fis);
									            createAuctionSmartContracts = (Hashtable<Long, CreateAuctionSmartContract>) ois.readObject();
									 
									            // get the high score out
									            return createAuctionSmartContracts.get(auctionId);
								        }
						        });
								    
		    }
		    catch (PrivilegedActionException pae) {
	
		    	Exception e = pae.getException();
				
		    	if (e instanceof IOException)
				
		    		System.err.println(); //throw (IOException) e;
					
		    	else
				
		    		System.err.println(); //throw (ClassNotFoundException) e;
					
		    }
									
							
							
			
			
			
			
			
			
			byte[] serialization = this.serialize();
			
			PrivateKey privateKey = null;
			
			byte[] signature = this.doSignature(serialization, privateKey);
			
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
				
				System.err.println("This Smart Contract for the Creation of an Auction isn't valid!!!");
				
			}
			
		}
		else {
			
			System.err.println("This Smart Contract for the Creation of an Auction it's expired!!!");
		
		}
		
	}

	
	
	@Override
	public void terminate() {
		// TODO Auto-generated method stub

	}

	
	private byte[] serialize() {
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = null;
		
		try {
	
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(this);
			objectOutputStream.flush();

			return byteArrayOutputStream.toByteArray();
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}   
	
		return null;
		
	}
	
	private byte[] doSignature(byte[] serialization, PrivateKey privateKey) {
		
		try {
		
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(privateKey);
			
			signature.update(serialization);
			
			return signature.sign();
		
		}
		catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			e.printStackTrace();
		}
		
		return null;
	
	}
	
	
	private static class Worker extends Thread {
		
		private final Process process;
		
		private Integer exit;
		
		private Worker(Process process) {
			
			this.process = process;
		
		}
		
		public void run() {
			try { 
		    
				exit = process.waitFor();
		    
			} 
			catch (InterruptedException ignore) {
		    
				return;
		    
			}
		  
		}  
		
	}
	
}
