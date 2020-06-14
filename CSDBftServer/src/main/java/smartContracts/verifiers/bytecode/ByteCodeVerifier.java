package smartContracts.verifiers.bytecode;

public interface ByteCodeVerifier {
	
	void verifyInterfaces() throws Exception;
	
	void verifyConstructors() throws Exception;
	
	void verifyFields() throws Exception;
	
	
	
}
