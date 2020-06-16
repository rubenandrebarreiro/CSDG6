package smartContracts.verifiers.bytecode;

import src.SmartContract;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

public class AuctionCreatorSmartContractByteCodeVerifier implements ByteCodeVerifier {

	private Class<?> auctionCreatorSmartContractClass;
	
	public AuctionCreatorSmartContractByteCodeVerifier(Class<?> auctionCreatorSmartContractClass) {
		
		this.auctionCreatorSmartContractClass = auctionCreatorSmartContractClass;
		
	}
	
	@Override
	public void verifyInterfaces() throws Exception {

		Class<?>[] interfacesFromClassLoaded = this.auctionCreatorSmartContractClass.getInterfaces();
		
		if (interfacesFromClassLoaded.length == 2) {
			
			if (!(interfacesFromClassLoaded[0].getName().equalsIgnoreCase(SmartContract.class.getName()))) {
			
				throw new Exception("Invalid Interface from this Smart Contract's Class File!!!!");
			
			}
			
			if (!(interfacesFromClassLoaded[1].getName().equalsIgnoreCase(Serializable.class.getName()))) {
				
				throw new Exception("Invalid Interface from this Smart Contract's Class File!!!!");
			
			}
			
		}
		else {
			
			throw new Exception("Number Invalid of Interfaces from this Smart Contract's Class file!!!!");
			
		}
		
	}

	@Override
	public void verifyConstructors() throws Exception {

		Constructor<?>[] constructorsFromClassLoaded = this.auctionCreatorSmartContractClass.getConstructors();
		
		if(constructorsFromClassLoaded.length == 1) {
			
			Constructor<?> uniqueConstructorFromClassLoaded = constructorsFromClassLoaded[0];
			
			Parameter[] parametersFromConstructor = uniqueConstructorFromClassLoaded.getParameters();
			
			if (parametersFromConstructor.length == 2) {
			
				parametersFromConstructor[0].getClass().getName().equalsIgnoreCase(Long.class.getName());
				parametersFromConstructor[1].getClass().getName().equalsIgnoreCase(String.class.getName());
				
			}
			else {
				
				throw new Exception("Invalid Number of Parameters in the Constructor from this Smart Contract's Class file!!!!");
				
			}
			
		}
		else {
			
			throw new Exception("Invalid Number of Constructors from this Smart Contract's Class file!!!!");
			
		}
		
	}

	@Override
	public void verifyFields() throws Exception {

		Field durabilityField = this.auctionCreatorSmartContractClass.getField("DURABILITY");
		
		if ( durabilityField != null ) {
			
			if ( !durabilityField.getClass().getName().equalsIgnoreCase(Long.class.getName())) {
				
				throw new Exception("Invalid of Structure of Fields in the Constructor from this Smart Contract's Class file!!!!");
				
			}
			
		}
		else {
			
			throw new Exception("Invalid of Structure of Fields in the Constructor from this Smart Contract's Class file!!!!");
			
		}
		
		
		Field auctionIdField = this.auctionCreatorSmartContractClass.getField("auctionId");
		
		if ( auctionIdField != null ) {
			
			if ( !auctionIdField.getClass().getName().equalsIgnoreCase(Long.class.getName())) {
				
				throw new Exception("Invalid of Structure of Fields in the Constructor from this Smart Contract's Class file!!!!");
				
			}
			
		}
		else {
			
			throw new Exception("Invalid of Structure of Fields in the Constructor from this Smart Contract's Class file!!!!");
			
		}
		
		
		Field usernameIdField = this.auctionCreatorSmartContractClass.getField("username");
		
		if ( usernameIdField != null ) {
			
			if ( !usernameIdField.getClass().getName().equalsIgnoreCase(String.class.getName())) {
				
				throw new Exception("Invalid of Structure of Fields in the Constructor from this Smart Contract's Class file!!!!");
				
			}
			
		}
		else {
			
			throw new Exception("Invalid of Structure of Fields in the Constructor from this Smart Contract's Class file!!!!");
			
		}
		
	}
	
}
