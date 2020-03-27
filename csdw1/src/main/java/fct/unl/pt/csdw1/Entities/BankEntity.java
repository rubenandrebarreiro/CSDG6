package fct.unl.pt.csdw1.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BankEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String userName, password;
    private Long amount;

    protected BankEntity(){};

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
}
