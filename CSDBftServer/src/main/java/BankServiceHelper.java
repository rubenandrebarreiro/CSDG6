import org.json.JSONObject;

import java.util.Optional;

public class BankServiceHelper {

    protected static BankEntity registerUser(String username, String password, Long amount, BankRepository bankRepo) {

        return bankRepo.newUserBankEntity(new BankEntity(username, password, amount));
    }

    protected static BankEntity findUser(String username, BankRepository bankRepo) {
            return bankRepo.findByUserName(username).get();
    }

    protected static Iterable<BankEntity> getAllBankAcc(BankRepository bankRepo) {
        return bankRepo.findAll();

    }
    
    protected static Iterable<AuctionEntity> getAllOpenedAuctions(BankRepository bankRepo){

        return bankRepo.findAllOpenedAuctions();

    }
    
    protected static Iterable<AuctionEntity> getAllClosedAuctions(BankRepository bankRepo){

        return bankRepo.findAllClosedAuctions();

    }
    
    protected static Iterable<BidEntity> getBidsFromAuction(Long id, BankRepository bankRepo){

        return bankRepo.findAllBidsFromAuction(id);

    }
    
    protected static Iterable<BidEntity> getBidsFromUser(String who, BankRepository bankRepo){

        return bankRepo.findAllBidsFromUser(who);
        
    }
    
    protected static BidEntity getBidsFromUser(Long id, BankRepository bankRepo){

        return bankRepo.checkWinnerBidFromAuction(id);
        
    }
    
    protected static JSONObject transferMoney(String from, String to, long amount, BankRepository bankRepo) {
        Optional<BankEntity> beFrom = bankRepo.findByUserName(from);

        if (beFrom.isPresent()) {

            Optional<BankEntity> beTo = bankRepo.findByUserName(to);

            if (beTo.isPresent()) {

                if (amount > 0) {

                    BankEntity b = beFrom.get();

                    if ((b.getAmount() - amount) >= 0) {

                        b.updateAmount(-amount);

                        bankRepo.newUserBankEntity(b);
                        b = beTo.get();

                        b.updateAmount(amount);
                        bankRepo.newUserBankEntity(b);

                        return new JSONObject().put("Success", "True");

                    } else {

                        return new JSONObject().put("error", "From account doesn't have enough money").put("errorID", 3);

                    }
                } else {

                    return new JSONObject().put("error", "Amount<0").put("errorID", 2);

                }

            } else {

                return new JSONObject().put("error", "To account doesn't exist").put("errorID", 1);

            }

        } else {

            return new JSONObject().put("error", "From account doesn't exist").put("errorID", 0);

        }

    }

    protected static JSONObject createAuction(Long id, String who, BankRepository bankRepo) {
    	
    	Optional<BankEntity> be = bankRepo.findByUserName(who);

        if (be.isPresent()) {

        	Optional<AuctionEntity> ae = bankRepo.findByAuctionID(id);
        	
           if (!ae.isPresent()) {
        	   
               bankRepo.createAuction(new AuctionEntity(id, who));

               return new JSONObject().put("Success", "True").put("id", id);

           }
           else {
        	   return new JSONObject().put("error", "Auction already exist " + id);
           }
        	
        } else

            return new JSONObject().put("error", "User not found " + who);
    	
    }
    
    protected static JSONObject closeAution(Long id, String who, BankRepository bankRepo) {
		
    	Optional<BankEntity> be = bankRepo.findByUserName(who);
    	
        if (be.isPresent()) {

        	Optional<AuctionEntity> ae = bankRepo.findByAuctionID(id);
        	
        	if (ae.isPresent()) {
        	   
        	   AuctionEntity auction = ae.get();
        		

    		   if(auction.getOwnerUsername().equalsIgnoreCase(who)) {
    			  
    			   if(!auction.isClosed()) {
            		   
            		   auction.close();
            		   
            		   bankRepo.closeAuction(auction);
            		
            		   return new JSONObject().put("Success", "True").put("id", id);
         			  
            	   }
            	   else {
            		   return new JSONObject().put("error", "Auction it's already closed " + id);
            	   }
      
    		   }
    		   else
    			   return new JSONObject().put("error", "User " + who + " doesn't own this auction " + id); 

        	}
        	else {
        		return new JSONObject().put("error", "Auction don't exist " + id);
        	}
        	
        } 
        else

            return new JSONObject().put("error", "User not found " + who);
    	
	}
    
    
    
    protected static JSONObject createMoney(String who, Long amount, BankRepository bankRepo) {

        Optional<BankEntity> be = bankRepo.findByUserName(who);

        if (be.isPresent()) {

            BankEntity b = be.get();

            b.updateAmount(amount);

            return new JSONObject().put("Success", "True").put("amount", b.getAmount());

        } else

            return new JSONObject().put("error", "User not found " + who);

    }

    protected static long currentAmount(String who, BankRepository bankRepo) {
        Optional<BankEntity> be = bankRepo.findByUserName(who);
        if (be.isPresent()) {
            BankEntity b = be.get();
            return b.getAmount();
        } else {

            return -1;

        }

    }

}