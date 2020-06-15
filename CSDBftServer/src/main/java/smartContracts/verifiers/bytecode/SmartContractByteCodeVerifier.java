package smartContracts.verifiers.bytecode;

public class SmartContractByteCodeVerifier {

	public SmartContractByteCodeVerifier() {
		
	}
	
	public static void verifyByteCode(Class<?> classObject, byte smartContractCode) throws Exception {
		
		ByteCodeVerifier byteCodeVerifier;
		
		switch (smartContractCode) {
			
			case 0:
				
				byteCodeVerifier = new AuctionCreatorSmartContractByteCodeVerifier(classObject);
				
				byteCodeVerifier.verifyInterfaces();
				byteCodeVerifier.verifyConstructors();
				byteCodeVerifier.verifyFields();
				
			break;

			default:
				break;
		}
		
	}
	
}
