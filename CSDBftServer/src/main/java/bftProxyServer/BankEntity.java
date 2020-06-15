package bftProxyServer;
import org.json.JSONObject;

import java.io.Serializable;

public class BankEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
    private String userName, password;
    private Long amount;

    public BankEntity(String userName, String password, Long amount){
        this.userName = userName;
        this.password = password;
        this.amount = amount;
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

    public JSONObject getJSON(){
        return new JSONObject().put("username",getOwnerName()).put("password",this.password).put("amount",getAmount());
    }

    public JSONObject getJSONSecure(){
        return new JSONObject().put("username",getOwnerName()).put("amount",getAmount());
    }
}
