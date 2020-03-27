package fct.unl.pt.csdw1.Daos;

public class BankAccountDao {
    public String username;
    public long amount;

    public BankAccountDao(String username, long amount){
        this.username = username;
        this.amount = amount;
    }
}
