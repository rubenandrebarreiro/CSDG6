package fct.unl.pt.csd.Contracts;

public interface SmartContract {

    public void init() throws Exception;

    public void run() throws Exception;

    public void terminate();

}
