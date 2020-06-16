package bftProxyServer;

import bftsmart.tom.MessageContext;
import src.SmartContract;

import java.util.concurrent.Future;

public class MappableContract {
//    public String who;
    public SmartContract contract;
    public Future<?> future;
//    public MessageContext messageContext;

    public MappableContract( SmartContract s,Future<?> f) {
//        this.who = who;
        this.contract = s;
        this.future = f;
//        this.messageContext = messageContext;
    }
}