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

    public BankEntity(String userName, String password, Long amount, String[] roles){
        this.userName = userName;
        this.password = password;
        this.amount = amount;
        this.roles = "ROLE_USER";
        for(int i = 0; i < roles.length;i++)
            if(!roles[i].equalsIgnoreCase("ROLE_USER"))
                this.roles += "@/&@"+roles[i];
    }


    public String getStringRoles(){
        return this.roles;
    }

    public ArrayList<String> getRoles(){
        String[] splitter = roles.split("@/&@");
        if(splitter.length <= 0)
            return null;
        return new ArrayList<>(Arrays.asList(splitter));
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
