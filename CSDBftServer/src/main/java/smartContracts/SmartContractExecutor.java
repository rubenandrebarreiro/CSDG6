package smartContracts;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SmartContractExecutor {

	private SmartContract smartContract;
	
	public SmartContractExecutor(SmartContract smartContract) {
		
		this.smartContract = smartContract;
		
	}
	
	public void start() {
	
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<?> future = executor.submit(smartContract);

		try {
		
			future.get(SmartContract.MAX_EXECUTION_TIME, TimeUnit.MILLISECONDS);
		    System.out.println("Smart Contract's Execution completed successfully!!!");
		
		}
		catch (InterruptedException interruptedException) {
		 
			System.out.println("Interruption Exception!!! Cancelling the runnable Smart Contract...");
		    future.cancel(true);
			
		}
		catch (ExecutionException executionException) {

		    System.out.println("Execution Exception!! Cancelling the runnable Smart Contract...");
		    future.cancel(true);
			
		}
		catch (TimeoutException timeoutException) {
			
		    System.out.println("Time Out Exception!!! Cancelling the runnable Smart Contract...");
		    future.cancel(true);
		
		}

		executor.shutdown();
		
	}
		
}
