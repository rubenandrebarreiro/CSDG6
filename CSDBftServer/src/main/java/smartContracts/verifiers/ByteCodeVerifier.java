package smartContracts.verifiers;

public interface ByteCodeVerifier {
	
	void verifyInterfaces() throws Exception;
	
	void verifyConstructors() throws Exception;
	
	void verifyFields() throws Exception;
	
	
	
}
