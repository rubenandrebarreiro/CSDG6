package src;

import java.util.List;

public interface SmartContract extends Runnable {

    public static final long MAX_EXECUTION_TIME = 600_000L;

    public void init() throws Exception;

    public void run();

    public void terminate();

    public void createMoney(int amount);
    public List<String> getOperations();
    public void clearOperations();
}
