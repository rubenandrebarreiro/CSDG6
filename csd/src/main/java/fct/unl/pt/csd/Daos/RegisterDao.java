package fct.unl.pt.csd.Daos;

public class RegisterDao {
    public String userName, password;
    public Long amount;
    public RegisterDao(String userName, String password, Long amount){
        this.userName = userName;
        this.password = password;
        this.amount = amount;
    }
}