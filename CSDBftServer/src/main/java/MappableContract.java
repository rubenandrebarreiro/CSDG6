import bftsmart.tom.MessageContext;
import src.SmartContract;

public class MappableContract {
    public String who;
    public SmartContract contract;
//    public MessageContext messageContext;

    public MappableContract(String who, SmartContract s) {
        this.who = who;
        this.contract = s;
//        this.messageContext = messageContext;
    }
}