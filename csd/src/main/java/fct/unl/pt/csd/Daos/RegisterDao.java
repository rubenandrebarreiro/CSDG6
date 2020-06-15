package fct.unl.pt.csd.Daos;

public class RegisterDao {
    public String userName, password;
    public Long amount;
    public String[] roles;

    public RegisterDao(String userName, String password, Long amount, String[] roles){
        this.userName = userName;
        this.password = password;
        this.amount = amount;
        this.roles = roles;
    }
}
