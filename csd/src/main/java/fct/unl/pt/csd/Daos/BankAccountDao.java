package fct.unl.pt.csd.Daos;

public class BankAccountDao {
    public String username;
    public long amount;

    public BankAccountDao(String username, long amount){
        this.username = username;
        this.amount = amount;
    }
}
