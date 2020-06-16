package src;

import src.src.SmartContract;

import java.util.*;

public class AuctionSmartContract implements SmartContract {

    static Map<Integer, List<String>> auctions;
    static int i=0;
    static boolean t;

    public void init() throws Exception {
        System.out.println("Init");
    }

    public void run() {
        System.out.println("Run");
    }

    public final void terminate() {
        t=false;
        System.out.println("Terminate");
    }


    public final int createAuction(){
        if(auctions == null)
            auctions = new HashMap<>();
        auctions.put(i,new ArrayList<>());
        return i++;
    }

    public final boolean makeManager(int id,String username){
        if(auctions == null)
            return false;
        if(auctions.containsKey(id)){
            auctions.get(id).add(username);
            return true;
        }else
            return false;

    }
}
