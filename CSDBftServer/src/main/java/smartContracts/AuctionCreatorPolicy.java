package smartContracts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AuctionCreatorPolicy {

	private File file;
	
	private StringBuilder stringBuilder;
	
	private static final String PATH = "policies/auction-creator.policy";
	
	
	public AuctionCreatorPolicy() {
		
		this.file = new File(PATH);
		
		this.stringBuilder = new StringBuilder();
		
	}
	
	public void init() {
		
		try {
			
			if(this.file.createNewFile()) {
				
				System.out.println("File created: " + this.file.getName());
				
				this.stringBuilder.append("keystore \"users.keystore\"\n\n");
				

				this.stringBuilder.append("grand SignedBy \"admin\" {\n");
				this.stringBuilder.append("		permission java.util.PropertyPermission\n");
				this.stringBuilder.append("			\"smartcontracts.auctioncreators\", \"read\"\n");
				this.stringBuilder.append("		permission java.util.PropertyPermission\n");
				this.stringBuilder.append("			\"smartcontracts.auctioncreators\", \"read,write\"\n");
				this.stringBuilder.append("		permission\n");
				this.stringBuilder.append("			\"smartcontracts.auctioncreators\", \"read,write\"\n");
				this.stringBuilder.append("			\"smartcontracts.auctioncreators\", \"read,write\"\n");
				
				this.stringBuilder.append("};");
				
				
				
				Files.write(Paths.get(PATH), this.stringBuilder.toString().getBytes(), StandardOpenOption.APPEND);
				
				this.stringBuilder = new StringBuilder();
				
			}
			else {
				
				System.err.println("File: " + this.file.getName() + " already exists!!!");
				
			}
			
		}
		catch (IOException ioException) {
			
			System.err.println("An error occurred during the creation of the Auction Creator's Policy: " + PATH);
			
		}
		
	}
	
	public void grantPolicies(String username) {
		
		try {
		
			this.stringBuilder.append("grand SignedBy \"" + username + "\" {\n");
			this.stringBuilder.append("		permision\n");
			this.stringBuilder.append("			\n");
			
			this.stringBuilder.append("};");
			
			
			Files.write(Paths.get(PATH), "the text".getBytes(), StandardOpenOption.APPEND);
		
		}
		catch (IOException e) {
		
			//exception handling left as an exercise for the reader
		
		}
		
	}
	
	public boolean verifySignatures(Object ... params) {
		
		if( ( params[0] instanceof String ) && ( params[1] instanceof Long) ) {
			
			ClassLoader classLoader = new Cl
			
		}
		else {
			
			System.err.println("Invalid parameters!!!");
			
			return false;
			
		}
		
	}
	
}
