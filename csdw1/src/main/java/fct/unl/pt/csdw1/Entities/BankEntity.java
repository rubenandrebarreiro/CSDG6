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
    private String ownerName;
    private Long amount;

    protected BankEntity(){};

    public BankEntity(String ownerName, Long amount){
        this.ownerName = ownerName;
        this.amount = amount;
    }

    public Long getID(){
        return id;
    }

    public String getOwnerName(){
        return ownerName;
    }

    public Long getAmount(){
        return amount;
    }
}
