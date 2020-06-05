package fct.unl.pt.csd.Repositories.Redis;
import java.util.HashSet;
import java.util.Map;
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
		
		boolean existHostAndPortString = this.jedis.sismember("replicas", hostAndPortString);
		
		
		if(!existHostAndPortString) {
			
			this.jedis.sadd("replicas", hostAndPortString);
		
			this.jedisClusterNodes.add(hostAndPort);
			
			
			return true;
			
		}
		
		return false;
	
	}
	
	private boolean addNewUser(String username, String password, Long amount) {
		
		Map<String, String> userEntry = jedis.hgetAll("users#" + username);
		
		
		if (userEntry.isEmpty()) {
			
			jedis.hset("users#" + username, "username", username);
			
			jedis.hset("users#" + username, "password", password);
			
			jedis.hset("users#" + username, "amount", String.valueOf(amount));
			
			
			System.out.println("REDIS Storage: New User registered!!!");
			
			
			return true;
			
		}
		else {
			
			System.err.println("REDIS Storage: User not registered!!! An User with this Username already exist!!!");
			
		}
		
		
		return false;
		
	}
	
	
	private boolean createMoney(String username, Long amount) {
		
		Map<String, String> userEntry = jedis.hgetAll("users#" + username);
		
		
		if (!userEntry.isEmpty()) {
			
			Long oldAmount = Long.parseLong(userEntry.get("amount"));
			
			Long newAmount = (amount + oldAmount);
			
			jedis.hset("users#" + username, "amount", String.valueOf(newAmount));
			
			
			System.out.println("REDIS Storage: New User was registered!!!");
			
			
			return true;
			
		}
		else {
			
			System.err.println("REDIS Storage: New User wasn't registered!!! An User with this Username already exist!!!");
			
		}
		
		return false;
		
	}
	
	
	private boolean transferMoney(String fromUsername, String toUsername, Long amount) {
		
		Map<String, String> fromUserEntry = jedis.hgetAll("users#" + fromUsername);
		
		Map<String, String> toUserEntry = jedis.hgetAll("users#" + toUsername);
		
		
		if (!fromUserEntry.isEmpty()) {
			
			if (!toUserEntry.isEmpty()) {
				
				Long oldAmountFromUser = Long.parseLong(fromUserEntry.get("amount"));
				
				Long newAmountFromUser = (oldAmountFromUser - amount);
				
				
				if ( newAmountFromUser >= 0L ) {
					
					Long oldAmountToUser = Long.parseLong(toUserEntry.get("amount"));
					
					Long newAmountToUser = (oldAmountFromUser + amount);
					
					
					jedis.hset("users#" + fromUsername, "amount", String.valueOf(newAmountFromUser));
					
					jedis.hset("users#" + toUsername, "amount", String.valueOf(newAmountToUser));
					
					
				
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
	
	private Long getMoney(String username) {
		
		Map<String, String> userEntry = jedis.hgetAll("users#" + username);
		
		
		if (!userEntry.isEmpty()) {
			
			Long amount = Long.parseLong(userEntry.get("amount"));
			
			return amount;
			
		}
		
		
		return null;
		
	}
	
	private boolean createAuction(String username, AuctionEntity auctionEntity) {
		
		Map<String, String> userEntry = jedis.hgetAll("users#" + username);
		
		
		if (!userEntry.isEmpty()) {
			
			Long auctionEntityID = auctionEntity.getID();
			
			Map<String, String> auction = jedis.hgetAll("auctions#" + auctionEntityID);
			
			
			if(auction.isEmpty()) {
				
				jedis.hset("auction#" + String.valueOf(auctionEntityID), "ownerUsername", username);
				jedis.hset("auction#" + String.valueOf(auctionEntityID), "state", "opened");
				jedis.hset("auction#" + String.valueOf(auctionEntityID), "numBids", String.valueOf(new Integer(0)));
								
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
	
	private boolean closeAuction(String username, AuctionEntity auctionEntity) {
		
		Map<String, String> userEntry = jedis.hgetAll("users#" + username);
		
		
		if (!userEntry.isEmpty()) {
			
			Long auctionEntityID = auctionEntity.getID();
			
			Map<String, String> auction = jedis.hgetAll("auctions#" + auctionEntityID);
			
			
			if (!auction.isEmpty()) {
				
				String ownerUsername = jedis.hget("auction#" + String.valueOf(auctionEntityID), "ownerUsername");
				
				if (ownerUsername.equalsIgnoreCase(username)) {
					
					jedis.hset("auction#" + String.valueOf(auctionEntityID), "state", "closed");
					
					
					System.out.println("REDIS Storage: The Auction was closed!!!");
					
					
					return true;
					
				}
				else {
				
					System.err.println("REDIS Storage: Auction wasn't closed!!! The User with the given Username doesn't own this Auction!!!");
					
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
		
		Map<String, String> userEntry = jedis.hgetAll("users#" + username);
		
		
		if (!userEntry.isEmpty()) {
			
			Long auctionEntityID = auctionEntity.getID();
			
			Map<String, String> auction = jedis.hgetAll("auctions#" + auctionEntityID);
			
			
			if (!auction.isEmpty()) {
				
				String ownerUsername = jedis.hget("auction#" + String.valueOf(auctionEntityID), "ownerUsername");
				
				if (!ownerUsername.equalsIgnoreCase(username)) {
					
					Long bidAmount = bidEntity.getAmount();
					
					
					if (auctionEntity.validBidAmount(bidAmount)) {
						
						
						
						
						System.out.println("REDIS Storage: Bid was added to the Auction!!! "
								         + "Bid #" + bidEntity.getID() + " added to the Auction #" + auctionEntityID + "!!!");
						
						
						return true;
					
					}
					
					
				}
				else {
				
					System.err.println("REDIS Storage: Bid wasn't added to the Auction!!! The User with the given Username doesn't own this Auction!!!");
					
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
	
	
	/**
	 * TODO - Remaining functions to implement:
	 * 1) CurrentOpenAuctions(): open-auctions
	 * 2) CurrentClosedAuctions(): closed-auctions
	 * 3) AuctionBids(Auction): bids
	 * 4) ClientBids(who): bids
	 * 5) CheckWinnerBidFromClosedAuction(Auction): bid
	 * 
	 */
	
}
