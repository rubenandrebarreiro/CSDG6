import org.json.JSONObject;

import java.util.Optional;

public class BankServiceHelper {


    protected static BankEntity registerUser(String username, String password, Long amount, String[] roles, BankRepository bankRepo) {
        return bankRepo.newUserBankEntity(new BankEntity(username, password, amount, roles));
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
    
    protected static BidEntity getWinnerBidFromAuction(Long id, BankRepository bankRepo){

        return bankRepo.checkWinnerBidFromAuction(id);
        
    }
    
    protected static JSONObject transferMoney(String from, long fromAmount, String to, long toAmount, BankRepository bankRepo) {
        Optional<BankEntity> beFrom = bankRepo.findByUserName(from);
        Optional<BankEntity> beTo = bankRepo.findByUserName(to);

        BankEntity b = beFrom.get();

        b.updateAmount(fromAmount);

        bankRepo.newUserBankEntity(b);
        b = beTo.get();

        b.updateAmount(toAmount);
        bankRepo.newUserBankEntity(b);

        return new JSONObject().put("Success", "True");
    }

    protected static JSONObject createAuction(Long id, String who, BankRepository bankRepo) {
    	
    	Optional<BankEntity> be = bankRepo.findByUserName(who);

        if (be.isPresent()) {

        	Optional<AuctionEntity> ae = bankRepo.findByAuctionID(id);
        	
           if (!ae.isPresent()) {
        	   
               bankRepo.createAuction(new AuctionEntity(id, who));
               be.get().addAuction(bankRepo.findByAuctionID(id).get());

               return new JSONObject().put("Success", "True").put("id", id);

           }
           else {
        	   return new JSONObject().put("error", "Auction already exist " + id);
           }
        	
        } else

            return new JSONObject().put("error", "User not found " + who);
    	
    }

    protected static JSONObject bid(BidEntity e, BankRepository bankRepo){
        Optional<BankEntity> be = bankRepo.findByUserName(e.getUsername());
        if (be.isPresent()) {
            Optional<AuctionEntity> ae = bankRepo.findByAuctionID(e.getID());
            if (ae.isPresent()) {
                if(ae.get().validBidAmount(e.getAmount())) {
                    bankRepo.createBid(e);
                    be.get().makeBidForAuction(ae.get(), e);
                    return new JSONObject().put("Success", "True").put("id", e.getID());
                }else
                    return new JSONObject().put("error", "Invalid Amount").put("id", e.getID());
            }else
                return new JSONObject().put("error", "Auction doesn't exist " + e.getID());
        }else

            return new JSONObject().put("error", "User not found " + e.getUsername());
    }

    protected static JSONObject closeAution(Long id, String who, BankRepository bankRepo) {
		
    	Optional<BankEntity> be = bankRepo.findByUserName(who);
    	
        if (be.isPresent()) {

        	Optional<AuctionEntity> ae = bankRepo.findByAuctionID(id);
        	Optional<AuctionEntity> oae = bankRepo.findByOpenedAuctionID(id);
        	Optional<AuctionEntity> cae = bankRepo.findByClosedAuctionID(id);
        	
        	if ( ( ae.isPresent() ) && ( oae.isPresent() ) && ( !cae.isPresent() ) ) {
        	   
        		AuctionEntity auction = ae.get();
        		AuctionEntity openedAuction = oae.get();

    		   if ( ( auction.getOwnerUsername().equalsIgnoreCase(who) ) && ( openedAuction.getOwnerUsername().equalsIgnoreCase(who) ) ) {
    			  
    			   if ( ( !auction.isClosed() ) && ( !openedAuction.isClosed() ) ) {
            		   
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
    
    protected static JSONObject createBid(Long bidId, Long auctionId, Long amount, String who, BankRepository bankRepo) {
		
    	Optional<BankEntity> be = bankRepo.findByUserName(who);
    	
        if (be.isPresent()) {

        	Optional<AuctionEntity> ae = bankRepo.findByAuctionID(bidId);
        	Optional<AuctionEntity> oae = bankRepo.findByOpenedAuctionID(bidId);
        	Optional<AuctionEntity> cae = bankRepo.findByClosedAuctionID(bidId);
        	
        	if ( ( ae.isPresent() ) ) {
        	   
        		if ( ( oae.isPresent() ) && ( !cae.isPresent() ) ) {
        			
        		   AuctionEntity auction = ae.get();
             	   AuctionEntity openedAuction = oae.get();
             	   
         		   if ( ( !auction.getOwnerUsername().equalsIgnoreCase(who) ) && ( !openedAuction.getOwnerUsername().equalsIgnoreCase(who) ) ) {
         			  
         			   if( ( !auction.isClosed() ) && ( !openedAuction.isClosed() ) ) {
                 		   	
         				   if (!auction.getBids().containsKey(bidId)) {
         					  
         					   BankEntity bankEntity = be.get();
         					   
         					   if(bankEntity.getAmount() >= amount) {
         						  
         						   BidEntity bid = new BidEntity(bidId, who, amount);
             					   bankEntity.makeBidForAuction(auction, bid);

             					   return new JSONObject().put("Success", "True").put("id", bidId);
         					   
         					   }
         					   else
              					  return new JSONObject().put("error", "User " + who + " don't have the necessary amount of money to make this bid");
         						
         				   }
         				   else
         					  return new JSONObject().put("error", "Auction " + auctionId + " already contains a Bid with thhe id " + bidId);
                 	   }
                 	   else {
                 		   return new JSONObject().put("error", "Auction it's already closed " + auctionId);
                 	   }
           
         		   }
         		   else
         			   return new JSONObject().put("error", "User " + who + " can't make bids for its own auction " + auctionId); 
        			
        		}
        		else
        			return new JSONObject().put("error", "Auction it's already closed " + auctionId);
        	}
        	else {
        		return new JSONObject().put("error", "Auction don't exist " + auctionId);
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

    public static JSONObject validateCode(byte[] code){

        //TODO:Implement
        return new JSONObject().put("SUCCESS",true);
    }

}