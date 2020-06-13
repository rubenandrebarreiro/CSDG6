package fct.unl.pt.csd.Entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;

@Entity
public class BankEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String userName, password;
    private Long amount;
    private String roles;

    protected BankEntity(){};

    public BankEntity(String userName, String password, Long amount){
        this.userName = userName;
        this.password = password;
        this.amount = amount;
        this.roles = "ROLE_USER";
    }

    public void setNewRole(String newRole){
        roles+="@/&@"+newRole;
    }

    public ArrayList<String> getRoles(){
        String[] splitter = roles.split("@/&@");
        if(splitter.length <= 0)
            return null;
        return (ArrayList<String>) Arrays.asList(splitter);
    }

    public Long getID(){
        return id;
    }

    public String getOwnerName(){
        return userName;
    }

    public String getPassword(){return password;}

    public Long getAmount(){
        return amount;
    }

    public void updateAmount(long amount){
        this.amount += amount;
    }

    public void changeAmount(long amount){
        this.amount = amount;
    }

    public JSONObject getJSONSecure(){
        return new JSONObject().put("username",getOwnerName()).put("amount",getAmount());
    }
}
