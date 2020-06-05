package fct.unl.pt.csd.Repositories.Redis;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fct.unl.pt.csd.Entities.AuctionEntity;
import fct.unl.pt.csd.Entities.BidEntity;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

public class BankServiceReplicationJedisCluster {
	
	private Jedis jedis;
	
	private JedisCluster jedisCluster;
	
	private Set<HostAndPort> jedisClusterNodes;
	
	
	public BankServiceReplicationJedisCluster() {
		
		this.jedis = new Jedis();
		
		this.jedisClusterNodes = new HashSet<HostAndPort>();
		
		this.jedisCluster = new JedisCluster(jedisClusterNodes);
		
	}
	
	public boolean addNewClusterNodeReplica(HostAndPort hostAndPort) {
		
		String hostAndPortString = hostAndPort.getHost() + ":" + hostAndPort.getPort();
		
		
		if ( !this.jedis.sismember("replicas", hostAndPortString) ) {
			
			this.jedis.sadd( "replicas", hostAndPortString );
		
			this.jedisClusterNodes.add(hostAndPort);
			
			this.jedis.save();
			
			return true;
			
		}
		
		return false;
	
	}
	
	
	public boolean addNewUser(String username, String password, Long amount) {
		
		Map<String, String> userEntry = this.jedis.hgetAll( "users#" + username );
		
		
		if ( ( userEntry.isEmpty() ) && ( !this.jedis.sismember("users-registered", username) ) ) {
			
			this.jedis.hset( "users#" + username, "username", username );
			
			this.jedis.hset( "users#" + username, "password", password );
			
			this.jedis.hset( "users#" + username, "amount", String.valueOf(amount) );
			
			
			this.jedis.sadd( "users-registered", username );
			
			
			this.jedis.save();
			
			
			System.out.println("REDIS Storage: New User registered!!!");
			
			
			return true;
			
		}
		else {
			
			System.err.println("REDIS Storage: User not registered!!! An User with this Username already exist!!!");
			
		}
		
		
		return false;
		
	}

	
	public boolean createMoney(String username, Long amount) {
		
		Map<String, String> userEntry = this.jedis.hgetAll("users#" + username);
		
		
		if ( ( !userEntry.isEmpty() ) && ( this.jedis.sismember( "users-registered", username ) ) ) {
			
			Long oldAmount = Long.parseLong(userEntry.get("amount"));
			
			Long newAmount = ( amount + oldAmount );
			
			this.jedis.hset( "users#" + username, "amount", String.valueOf(newAmount) );
			
			
			this.jedis.save();
			
			
			System.out.println("REDIS Storage: New User was registered!!!");
			
			
			return true;
			
		}
		else {
			
			System.err.println("REDIS Storage: New User wasn't registered!!! An User with this Username already exist!!!");
			
		}
		
		return false;
		
	}
	
	
	public boolean transferMoney(String fromUsername, String toUsername, Long amount) {
		
		Map<String, String> fromUserEntry = this.jedis.hgetAll("users#" + fromUsername);
		
		Map<String, String> toUserEntry = this.jedis.hgetAll("users#" + toUsername);
		
		
		if ( ( !fromUserEntry.isEmpty() ) && ( this.jedis.sismember( "users-registered", fromUsername ) ) ) {
			
			if ( ( !toUserEntry.isEmpty() ) && ( this.jedis.sismember( "users-registered", toUsername ) ) ) {
				
				Long oldAmountFromUser = Long.parseLong( fromUserEntry.get("amount") );
				
				Long newAmountFromUser = ( oldAmountFromUser - amount );
				
				
				if ( newAmountFromUser >= 0L ) {
					
					Long oldAmountToUser = Long.parseLong( toUserEntry.get("amount") );
					
					Long newAmountToUser = ( oldAmountToUser + amount );
					
					
					this.jedis.hset( "users#" + fromUsername, "amount", String.valueOf(newAmountFromUser) );
					
					this.jedis.hset( "users#" + toUsername, "amount", String.valueOf(newAmountToUser) );
					
					
					this.jedis.save();
					
				
					System.err.println("REDIS Storage: Transfer of Money was successful!!!");
					
					
					return true;
					
				}
				else {
					
					System.err.println("REDIS Storage: Transfer of Money wasn't successful!!! The Sender User of the Transfer, don't have enough money!!!");
					
				}
			
			}
			else {
				
				System.err.println("REDIS Storage: Transfer of Money wasn't successful!!! The Receiver User of the Transfer, with the given Username doesn't exist!!!");
				
			}
				
		}
		else {
			
			System.err.println("REDIS Storage: Transfer of Money wasn't successful!!! The Sender User of the Transfer, with the given Username doesn't exist!!!");
			
		}
		
		
		return false;
		
	}
	
	
	public Long getMoney(String username) {
		
		Map<String, String> userEntry = this.jedis.hgetAll("users#" + username);
		
		
		if ( ( !userEntry.isEmpty() ) && ( this.jedis.sismember( "users-registered", username ) ) ) {
			
			Long amount = Long.parseLong(userEntry.get("amount"));
			
			return amount;
			
		}
		
		
		return null;
		
	}
	
	
	public boolean createAuction(String username, AuctionEntity auctionEntity) {
		
		Map<String, String> userEntry = this.jedis.hgetAll("users#" + username);
		
		
		if ( ( !userEntry.isEmpty() ) && ( this.jedis.sismember( "users-registered", username ) ) ) {
			
			Long auctionEntityID = auctionEntity.getID();
			
			Map<String, String> auction = this.jedis.hgetAll("auctions#" + auctionEntityID);
			
			
			if ( ( auction.isEmpty() ) && ( !this.jedis.sismember( "auctions", String.valueOf(auctionEntityID) ) ) ) {
				
				this.jedis.hset( "auction#" + String.valueOf(auctionEntityID), "ownerUsername", username );
				
				this.jedis.hset( "auction#" + String.valueOf(auctionEntityID), "state", "opened" );
				
				this.jedis.hset( "auction#" + String.valueOf(auctionEntityID), "numBids", String.valueOf(new Integer(0)) );
				

				this.jedis.sadd( "auctions", String.valueOf(auctionEntityID) );
				
				this.jedis.sadd( "opened-auctions", String.valueOf(auctionEntityID) );
				
				
				this.jedis.save();
				
				
				System.out.println("REDIS Storage: Auction created!!! Auction #" + auctionEntityID + " created by: " + username + "!!!");
				
				
				return true;
				
			}
			else {
				
				System.err.println("REDIS Storage: Auction not created!!! Already exist an Auction with the given ID!!!");
				
			}
			
		}
		else {

			System.err.println("REDIS Storage: Auction not created!!! Username doesn't exist!!!");
			
		}
		
		return false;
		
	}
	
	public boolean closeAuction(String username, AuctionEntity auctionEntity) {
		
		Map<String, String> userEntry = this.jedis.hgetAll("users#" + username);
		
		
		if ( ( !userEntry.isEmpty() ) && ( this.jedis.sismember( "users-registered", username ) ) ) {
			
			Long auctionEntityID = auctionEntity.getID();
			
			Map<String, String> auction = this.jedis.hgetAll("auctions#" + auctionEntityID);
			
			
			if ( ( !auction.isEmpty() ) && ( this.jedis.sismember( "auctions", String.valueOf(auctionEntityID) ) ) ) {
				
				if ( ( this.jedis.sismember( "opened-auctions", String.valueOf(auctionEntityID) ) ) && ( !this.jedis.sismember( "closed-auctions", String.valueOf(auctionEntityID) ) ) ) {
				
					String ownerUsername = this.jedis.hget("auction#" + String.valueOf(auctionEntityID), "ownerUsername");
					
					
					if (ownerUsername.equalsIgnoreCase(username)) {
						
						
						this.jedis.hset("auction#" + String.valueOf(auctionEntityID), "state", "closed");
						
						
						this.jedis.srem( "opened-auctions" , String.valueOf(auctionEntityID) );
						
						
						this.jedis.sadd( "closed-auctions" , String.valueOf(auctionEntityID) );
						
						
						this.jedis.save();
						
						
						System.out.println("REDIS Storage: The Auction was closed!!!");
						
						
						return true;
						
					}
					else {
					
						System.err.println("REDIS Storage: Auction wasn't closed!!! The User with the given Username doesn't own this Auction!!!");
						
					}
				
				}
				else {
					
					System.err.println("REDIS Storage: Auction wasn't closed!!! This Auction it's already closed!!!");
					
				}
				
			}
			else {
				
				System.err.println("REDIS Storage: Auction wasn't closed!!! Don't exist an Auction with the given ID!!!");
				
			}
			
		}
		else {

			System.err.println("REDIS Storage: Auction wasn't closed!!! Username doesn't exist!!!");
			
		}
		
		return false;
		
	}
	
	public boolean addBidToAuction(String username, AuctionEntity auctionEntity, BidEntity bidEntity) {
		
		Map<String, String> userEntry = this.jedis.hgetAll("users#" + username);
		
		
		if ( ( !userEntry.isEmpty() ) && ( this.jedis.sismember( "users-registered", username ) ) ) {
			
			Long auctionEntityID = auctionEntity.getID();
			
			Map<String, String> auction = this.jedis.hgetAll("auctions#" + auctionEntityID);
			
			
			if ( ( !auction.isEmpty() ) && ( this.jedis.sismember( "auctions", String.valueOf(auctionEntityID) ) ) ) {
				
				if ( ( this.jedis.sismember( "opened-auctions", String.valueOf(auctionEntityID) ) ) && ( !this.jedis.sismember( "closed-auctions", String.valueOf(auctionEntityID) ) ) ) {
					
					String ownerUsername = this.jedis.hget("auction#" + String.valueOf(auctionEntityID), "ownerUsername");
					
					if (!ownerUsername.equalsIgnoreCase(username)) {
						
						Long bidAmount = bidEntity.getAmount();
						
						
						if (auctionEntity.validBidAmount(bidAmount)) {
							
							Long bidID = bidEntity.getID();
							
							
							this.jedis.sadd( "bids" , String.valueOf(bidID) );
							
							
							if ( this.jedis.hget( "auction#" + String.valueOf(auctionEntityID), "bids" ) == null ) {
								
								this.jedis.hset( "auction#" + String.valueOf(auctionEntityID), "bids", bidEntity.toString());
								
							}
							else {
								
								String oldBids = this.jedis.hget( "auction#" + String.valueOf(auctionEntityID), "bids");
								
								String updatedBids = oldBids + " | " + bidEntity.toString();
								
								
								this.jedis.hset( "auction#" + String.valueOf(auctionEntityID), "bids", updatedBids);
								
							}
							
							
							this.jedis.save();
							
							
							System.out.println("REDIS Storage: Bid was added to the Auction!!! "
									         + "Bid #" + bidEntity.getID() + " added to the Auction #" + auctionEntityID + "!!!");
							
							
							return true;
						
						}
						else {
							
							System.err.println("REDIS Storage: Bid wasn't added to the Auction!!! "
								   	         + "The Bid's Amount for this Auction needs to be higher than the current highest one!!!");
							
						}
						
					}
					else {
					
						System.err.println("REDIS Storage: Bid wasn't added to the Auction!!! The User with the given Username doesn't own this Auction!!!");
						
					}
					
				}
				else {
					
					System.err.println("REDIS Storage: Bid wasn't added to the Auction!!! It's only possible to add Bids to Opened Auctions!!!");
					
				}
				
			}
			else {
				
				System.err.println("REDIS Storage: Bid wasn't added to the Auction!!! Don't exist an Auction with the given ID!!!");
				
			}
			
		}
		else {

			System.err.println("REDIS Storage: Bid wasn't added to the Auction!!! Username doesn't exist!!!");
			
		}
		
		return false;
		
	}
	
	public void getCurrentOpenAuctions() {
		
		Set<String> openedAuctions = this.jedis.smembers( "opened-auctions" );
		
		
		for ( String openedAuctionID : openedAuctions ) {
			
			System.out.println("INFO for Auction #" + openedAuctionID);
			
			
			Map<String, String> openedAuctionFields = this.jedis.hgetAll( "auction#" + openedAuctionID );
			
			
			for ( Entry<String, String> openedAuctionFieldsEntry : openedAuctionFields.entrySet() ) {
				
				System.out.println();
				
				System.out.println(" - " + openedAuctionFieldsEntry.getKey() + ":");
				
				System.out.println("    -> " + openedAuctionFieldsEntry.getValue() + ";");
				
			}
			
		}
		
	}
	
	public void getCurrentClosedAuctions() {
		
		Set<String> closedAuctions = this.jedis.smembers( "closed-auctions" );
		
		
		for ( String closedAuctionID : closedAuctions ) {
			
			System.out.println("INFO for Auction #" + closedAuctionID);
			
			
			Map<String, String> closedAuctionFields = this.jedis.hgetAll( "auction#" + closedAuctionID );
			
			
			for ( Entry<String, String> closedAuctionFieldsEntry : closedAuctionFields.entrySet() ) {
				
				System.out.println();
				
				System.out.println(" - " + closedAuctionFieldsEntry.getKey() + ":");
				
				System.out.println("    -> " + closedAuctionFieldsEntry.getValue() + ";");
				
			}
			
		}
		
	}
	
	
	/**
	 * TODO - Remaining functions to implement:
	 * 1) AuctionBids(Auction): bids
	 * 2) ClientBids(who): bids
	 * 3) CheckWinnerBidFromClosedAuction(Auction): bid
	 * 
	 */
	
}
