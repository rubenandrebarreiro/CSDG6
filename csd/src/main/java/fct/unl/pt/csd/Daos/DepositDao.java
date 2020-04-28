package fct.unl.pt.csd.Daos;

public class DepositDao {
    public String who;
    public Long amount;

    public DepositDao(String who, Long amount){
        this.who = who;
        this.amount = amount;
    }
}
