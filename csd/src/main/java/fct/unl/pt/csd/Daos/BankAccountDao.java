package fct.unl.pt.csd.Daos;

public class BankAccountDao {
    public String username;
    public Long amount;

    public BankAccountDao(String username, Long amount){
        this.username = username;
        this.amount = amount;
    }
}
