package src;

public interface SmartContract {

    void init() throws Exception;

    void run() throws Exception;

    void terminate();

}
