package fct.unl.pt.csd.Daos;

public class CreateMoneyDao {

    public Long amount = (long)0;

    public CreateMoneyDao(String amount){
        this.amount = Long.parseLong(amount);
    }
    public CreateMoneyDao(){}
}
