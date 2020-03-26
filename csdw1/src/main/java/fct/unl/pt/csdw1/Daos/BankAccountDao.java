package fct.unl.pt.csdw1.Daos;

public class BankAccountDao {

    public String owner;
    public Long amount;
    public BankAccountDao(String owner, Long amount){
        this.owner = owner;
        this.amount = amount;
    }
}
