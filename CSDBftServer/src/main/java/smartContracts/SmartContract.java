package smartContracts;

public interface SmartContract extends Runnable {
	
	public void init() throws Exception;
	
	public void run();
	
	public void terminate();
	
}
