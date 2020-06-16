package fct.unl.pt.csd.Controller;

import bftsmart.reconfiguration.util.Configuration;
import bftsmart.reconfiguration.util.DefaultRSAKeyLoader;
import bftsmart.reconfiguration.util.RSAKeyUtils;
import bftsmart.tom.ServiceProxy;
import bftsmart.tom.core.messages.TOMMessage;
import bftsmart.tom.util.Extractor;
import bftsmart.tom.util.TOMUtil;
import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Entities.BidEntity;
import fct.unl.pt.csd.Security.MyUserDetails;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ClientRequestHandler implements UserDetailsService {

    private ServiceProxy serviceProxy;

    @Autowired
    public ClientRequestHandler() {

        //this.username = username;
        // TODO ID Client??
//        this.serviceProxy = new ServiceProxy(1014, "config");
        Comparator<byte[]> replyComparator = new Comparator<byte[]>() {

			@Override
			public int compare(byte[] o1, byte[] o2) {
			    if(o1!=null && o2!=null) {
                    byte[] o1Content = new byte[o1.length - 128];
                    byte[] o2Content = new byte[o2.length - 128];

                    System.arraycopy(o1, 0, o1Content, 0, (o1.length - 128));
                    System.arraycopy(o2, 0, o2Content, 0, (o2.length - 128));

                    try {
                    ByteArrayInputStream receivedRequestByteArrayInputStream = new ByteArrayInputStream(o1Content);
                    ObjectInput receivedRequestObjectInput = new ObjectInputStream(receivedRequestByteArrayInputStream);
                    ByteArrayInputStream receivedRequestByteArrayInputStream2 = new ByteArrayInputStream(o2Content);
                    ObjectInput receivedRequestObjectInput2 = new ObjectInputStream(receivedRequestByteArrayInputStream2);
                        Object s1 = receivedRequestObjectInput.readObject();
                        Object s2 =  receivedRequestObjectInput2.readObject();
                    return (s1).equals(s2)?0:1;
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return (Arrays.equals(o1Content, o2Content) == true) ? 0 : 1;
                }else{
                    return (Arrays.equals(o1, o2) == true) ? 0 : 1;
                }

			}
			
		};
		
        Extractor replyExtractor = new Extractor() {
			
			@Override
			public TOMMessage extractResponse(TOMMessage[] replies, int sameContent, int lastReceived) {

				int numValidReplies = 0;
				
				for(TOMMessage reply : replies) {
				    if(reply!=null){
					byte[] replyBytes = reply.getContent();
					
					//byte[] replyBytes = TOMMessage.messageToBytes(reply);
					
					try {
                        byte[] encoded = Files.readAllBytes(Paths.get("config/keys/publickey" + reply.getSender()));
                        String key = new String(encoded);
                        PublicKey publicKey = RSAKeyUtils.getPublicKeyFromString(key);
                        byte[] replyContentBytes = new byte[(replyBytes.length - 128)];
                        byte[] replySignatureBytes = new byte[128];

                        System.arraycopy(replyBytes, 0, replyContentBytes, 0, replyContentBytes.length);
                        System.arraycopy(replyBytes, (replyBytes.length - 128), replySignatureBytes, 0, 128);

                        //Signature signature = Signature.getInstance("SHA256withRSA");

                        //signature.initVerify(publicKey);
//                        boolean isReplyValid = TOMUtil.verifySignature(publicKey, replyContentBytes, replySignatureBytes);

                        Signature sha_rsa = Signature.getInstance("SHA512withRSA");
                        sha_rsa.initVerify(publicKey);
                        sha_rsa.update(replyContentBytes);
                        boolean isReplyValid = sha_rsa.verify(replySignatureBytes);
                        if (isReplyValid) {

                            numValidReplies++;

                        }
					}
					catch (Exception exception) {
						
						exception.printStackTrace();
					
					}
                    }
				}
				
				int fPlus1Consensus = ( serviceProxy.getViewManager().getCurrentView().getF()*2 + 1 );
				
				if(numValidReplies >= fPlus1Consensus) {
				
					TOMMessage lastReply = replies[lastReceived];
	
					byte[] lastReplyBytes = lastReply.getContent();
					
					byte[] lastReplyContent = new byte[lastReplyBytes.length - 128];
					
					System.arraycopy(lastReplyBytes, 0, lastReplyContent, 0, (lastReplyBytes.length - 128) );
					
					return new TOMMessage(lastReply.getSender(), lastReply.getSession(), replies[lastReceived].getSequence(), replies[lastReceived].getOperationId(),
										  lastReplyContent, replies[lastReceived].getViewID(), replies[lastReceived].getReqType());
				
				}
				else {
					
					System.err.println("Invalid Consensus Reply: The Consensus wasn't obtained by at least f+1 Replicas : " +  fPlus1Consensus);
					
					return null;
					
				}
				
			}
			
        };

        this.serviceProxy = new ServiceProxy(1014,"config/system.config","config/hosts.config","config",replyComparator,replyExtractor);

//        this.serviceProxy = new ServiceProxy(1014, "config/system.config", "config/hosts.config", "config/keys/", replyComparator, replyExtractor);
        
        
    }


    protected String invokeCreateNew(String username, String password, Long amount, String[] roles) {

        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("REGISTER_USER");
            requestToSendObjectOutput.writeObject(username);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            requestToSendObjectOutput.writeObject(passwordEncoder.encode(password));
            requestToSendObjectOutput.writeObject(amount);
            requestToSendObjectOutput.writeObject(roles);
            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();
            
            byte[] requestReply = this.serviceProxy.invokeOrdered(requestToSendByteArrayOutputStream.toByteArray());
            
            if (requestReply == null) {

                System.err.println("It wasn't obtained the Consensus by, at least, f+1 Replicas!!!");

                return "";
                
            }
            
            if (requestReply.length == 0) {

                return "";

            }

            try
                    (
                            ByteArrayInputStream receivedRequestReplyByteArrayInputStream =
                                    new ByteArrayInputStream(requestReply);

                            ObjectInput receivedRequestReplyObjectInput =
                                    new ObjectInputStream(receivedRequestReplyByteArrayInputStream)
                    ) {

                return receivedRequestReplyObjectInput.readObject().toString();

            }

        } catch (IOException | ClassNotFoundException registerUserException) {

            System.out.println("Exception in registration of a New User/Client: " +
                    registerUserException.getMessage());

        }

        return "";

    }

    protected JSONObject invokeCreateMoney(String who, Long intitialAmount, Long finalAmount) {
        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("CREATE_MONEY");
            requestToSendObjectOutput.writeObject(who);
            requestToSendObjectOutput.writeLong(intitialAmount);
            requestToSendObjectOutput.writeLong(finalAmount);
            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            byte[] requestReply = this.serviceProxy.invokeOrdered(requestToSendByteArrayOutputStream.toByteArray());

            
            if (requestReply == null) {

                System.err.println("It wasn't obtained the Consensus by, at least, f+1 Replicas!!!");

                return null;
                
            }
            
            if (requestReply.length == 0) {

                return null;

            }

            try
                    (
                            ByteArrayInputStream receivedRequestReplyByteArrayInputStream =
                                    new ByteArrayInputStream(requestReply);

                            ObjectInput receivedRequestReplyObjectInput =
                                    new ObjectInputStream(receivedRequestReplyByteArrayInputStream)
                    ) {

                return new JSONObject(receivedRequestReplyObjectInput.readObject().toString());

            }

        } catch (IOException | ClassNotFoundException createMoneyException) {

            System.out.println("Exception in creation of Money by User/Client: " +
                    createMoneyException.getMessage());

        }

        return null;

    }

    protected JSONObject invokeTransferMoney(String from, long fromSaldo, String to, long toSaldo, long fromAmount, long toAmount ) {

        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("TRANSFER_MONEY");
            requestToSendObjectOutput.writeObject(from);
            requestToSendObjectOutput.writeLong(fromSaldo);
            requestToSendObjectOutput.writeObject(to);
            requestToSendObjectOutput.writeLong(toSaldo);
            requestToSendObjectOutput.writeLong(fromAmount);
            requestToSendObjectOutput.writeLong(toAmount);
            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            byte[] requestReply = this.serviceProxy.invokeOrdered(requestToSendByteArrayOutputStream.toByteArray());

            if (requestReply.length == 0) {

                return null;

            }

            try
                    (
                            ByteArrayInputStream receivedRequestReplyByteArrayInputStream =
                                    new ByteArrayInputStream(requestReply);

                            ObjectInput receivedRequestReplyObjectInput =
                                    new ObjectInputStream(receivedRequestReplyByteArrayInputStream)
                    ) {

                return new JSONObject(receivedRequestReplyObjectInput.readObject().toString());

            }

        } catch (IOException | ClassNotFoundException transferMoneyException) {

            System.out.println("Exception in Transfer Money From an User/Client to another User/Client: " +
                    transferMoneyException.getMessage());

        }

        return null;

    }
    
    protected JSONObject invokeCreateAuction(String who, Long auctionID) {
        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("CREATE_AUCTION");
            requestToSendObjectOutput.writeObject(who);
            requestToSendObjectOutput.writeObject(auctionID);

            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            byte[] requestReply = this.serviceProxy.invokeOrdered(requestToSendByteArrayOutputStream.toByteArray());

            if (requestReply.length == 0) {

                return null;

            }

            try
                    (
                            ByteArrayInputStream receivedRequestReplyByteArrayInputStream =
                                    new ByteArrayInputStream(requestReply);

                            ObjectInput receivedRequestReplyObjectInput =
                                    new ObjectInputStream(receivedRequestReplyByteArrayInputStream)
                    ) {

                return new JSONObject(receivedRequestReplyObjectInput.readObject().toString());

            }

        } catch (IOException | ClassNotFoundException createAuctionException) {

            System.out.println("Exception in creation of Auction by User/Client: " +
            		createAuctionException.getMessage());

        }

        return null;

    }
    
    protected JSONObject invokeCloseAuction(String who, Long auctionID) {
        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("CLOSE_AUCTION");
            requestToSendObjectOutput.writeObject(who);
            requestToSendObjectOutput.writeObject(auctionID);
            
            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            byte[] requestReply = this.serviceProxy.invokeOrdered(requestToSendByteArrayOutputStream.toByteArray());

            if (requestReply.length == 0) {

                return null;

            }

            try
                    (
                            ByteArrayInputStream receivedRequestReplyByteArrayInputStream =
                                    new ByteArrayInputStream(requestReply);

                            ObjectInput receivedRequestReplyObjectInput =
                                    new ObjectInputStream(receivedRequestReplyByteArrayInputStream)
                    ) {

                return new JSONObject(receivedRequestReplyObjectInput.readObject().toString());

            }

        } catch (IOException | ClassNotFoundException closeAuctionException) {

            System.out.println("Exception in closing an Auction by User/Client: " +
            		closeAuctionException.getMessage());

        }

        return null;

    }
    
    protected JSONObject invokeCreateBid(String who, Long auctionID, Long bidID, Long amount) {
        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("CREATE_BID");
            requestToSendObjectOutput.writeObject(who);
            requestToSendObjectOutput.writeObject(auctionID);
            requestToSendObjectOutput.writeObject(bidID);
            requestToSendObjectOutput.writeObject(amount);
            
            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            byte[] requestReply = this.serviceProxy.invokeOrdered(requestToSendByteArrayOutputStream.toByteArray());

            if (requestReply.length == 0) {

                return null;

            }

            try
                    (
                            ByteArrayInputStream receivedRequestReplyByteArrayInputStream =
                                    new ByteArrayInputStream(requestReply);

                            ObjectInput receivedRequestReplyObjectInput =
                                    new ObjectInputStream(receivedRequestReplyByteArrayInputStream)
                    ) {

                return new JSONObject(receivedRequestReplyObjectInput.readObject().toString());

            }

        } catch (IOException | ClassNotFoundException createBidException) {

            System.out.println("Exception in creation of the Bid for an Auction by User/Client: " +
                    createBidException.getMessage());

        }

        return null;

    }
    
    protected BankEntity invokeFindUser(String username) {

        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("FIND_USER");
            requestToSendObjectOutput.writeObject(username);

            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            byte[] requestReply = this.serviceProxy.invokeUnordered(requestToSendByteArrayOutputStream.toByteArray());

            
            if (requestReply == null) {

                System.err.println("It wasn't obtained the Consensus by, at least, f+1 Replicas!!!");

                return null;
                
            }
            
            if (requestReply.length == 0) {

                return null;

            }

            try
                    (
                            ByteArrayInputStream receivedRequestReplyByteArrayInputStream =
                                    new ByteArrayInputStream(requestReply);

                            ObjectInput receivedRequestReplyObjectInput =
                                    new ObjectInputStream(receivedRequestReplyByteArrayInputStream)
                    ) {

                JSONObject j = new JSONObject(receivedRequestReplyObjectInput.readObject().toString());
                return new BankEntity(j.getString("username"),j.getString("password"),j.getLong("amount"), j.getString("roles").split("@/&@"));

            }

        } catch (IOException | ClassNotFoundException findingAnUserException) {

            System.out.println("Exception in finding an User/Client: " +
                    findingAnUserException.getMessage());

        }

        return null;

    }

    protected JSONObject invokeListAllBankAccounts(int myHash) {

        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("LIST_ALL_BANK_ACCOUNTS");

            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            byte[] requestReply = this.serviceProxy.invokeUnordered(requestToSendByteArrayOutputStream.toByteArray());

                        
            if (requestReply == null) {

                System.err.println("It wasn't obtained the Consensus by, at least, f+1 Replicas!!!");

                return null;
                
            }
            
            if (requestReply.length == 0) {

                return null;

            }

            try
                    (
                            ByteArrayInputStream receivedRequestReplyByteArrayInputStream =
                                    new ByteArrayInputStream(requestReply);

                            ObjectInput receivedRequestReplyObjectInput =
                                    new ObjectInputStream(receivedRequestReplyByteArrayInputStream)
                    ) {



                int hash = (int) receivedRequestReplyObjectInput.readObject();
                if(hash != myHash) {
                    JSONArray arr = new JSONArray((String) receivedRequestReplyObjectInput.readObject());

                /*List<JSONObject> usersBankEntities = new ArrayList<>();
                System.out.println(numTotalUserBankEntities);
                for (int currentUserBankEntity = 0; currentUserBankEntity < numTotalUserBankEntities; currentUserBankEntity++) {
                    JSONObject j = new JSONObject(receivedRequestReplyObjectInput.readObject().toString());
                    System.out.println(j.toString());
                    usersBankEntities.add(j);
                }*/

                    return new JSONObject().put("arr",arr);
                }else
                    return new JSONObject();

            }

        } catch (IOException | ClassNotFoundException listAllBankAccountsException) {

            System.out.println("Exception in Listing All Bank Accounts: " +
                    listAllBankAccountsException.getMessage());

        }

        return null;

    }

    protected Long invokeCheckCurrentAmount(String username) {

        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("CHECK_CURRENT_AMOUNT");
            requestToSendObjectOutput.writeObject(username);

            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            byte[] requestReply = this.serviceProxy.invokeUnordered(requestToSendByteArrayOutputStream.toByteArray());

                        
            if (requestReply == null) {

                System.err.println("It wasn't obtained the Consensus by, at least, f+1 Replicas!!!");

                return null;
                
            }
            
            if (requestReply.length == 0) {

                return null;

            }

            try
                    (
                            ByteArrayInputStream receivedRequestReplyByteArrayInputStream =
                                    new ByteArrayInputStream(requestReply);

                            ObjectInput receivedRequestReplyObjectInput =
                                    new ObjectInputStream(receivedRequestReplyByteArrayInputStream)
                    ) {

                return (long) receivedRequestReplyObjectInput.readObject();

            }

        } catch (IOException | ClassNotFoundException checkCurrentAmountException) {

            System.out.println("Exception in checking the current Amount of a User/Client: " +
                    checkCurrentAmountException.getMessage());

        }

        return null;

    }
    
    protected Iterable<JSONObject> invokeListAllCurrentOpenedAuctions() {

        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("LIST_ALL_CURRENT_OPENED_AUCTIONS");

            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            byte[] requestReply = this.serviceProxy.invokeUnordered(requestToSendByteArrayOutputStream.toByteArray());

            if (requestReply.length == 0) {

                return null;

            }

            try
                    (
                            ByteArrayInputStream receivedRequestReplyByteArrayInputStream =
                                    new ByteArrayInputStream(requestReply);

                            ObjectInput receivedRequestReplyObjectInput =
                                    new ObjectInputStream(receivedRequestReplyByteArrayInputStream)
                    ) {

                int numTotalOpenedAuctionEntities = (int) receivedRequestReplyObjectInput.readObject();

                List<JSONObject> openedAuctionEntities = new ArrayList<>();
                System.out.println(numTotalOpenedAuctionEntities);
                for (int currentOpenedAuctionEntity = 0; currentOpenedAuctionEntity < numTotalOpenedAuctionEntities; currentOpenedAuctionEntity++) {
                    JSONObject j = new JSONObject(receivedRequestReplyObjectInput.readObject().toString());
                    System.out.println(j.toString());
                    openedAuctionEntities.add(j);
                }

                return openedAuctionEntities;

            }

        } catch (IOException | ClassNotFoundException listAllOpenedAuctionsException) {

            System.out.println("Exception in Listing All Current Opened Auctions: " +
            		listAllOpenedAuctionsException.getMessage());

        }

        return null;

    }
    
    protected Iterable<JSONObject> invokeListAllCurrentClosedAuctions() {

        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("LIST_ALL_CURRENT_CLOSED_AUCTIONS");

            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            byte[] requestReply = this.serviceProxy.invokeUnordered(requestToSendByteArrayOutputStream.toByteArray());

            if (requestReply.length == 0) {

                return null;

            }

            try
                    (
                            ByteArrayInputStream receivedRequestReplyByteArrayInputStream =
                                    new ByteArrayInputStream(requestReply);

                            ObjectInput receivedRequestReplyObjectInput =
                                    new ObjectInputStream(receivedRequestReplyByteArrayInputStream)
                    ) {

                int numTotalClosedAuctionEntities = (int) receivedRequestReplyObjectInput.readObject();

                List<JSONObject> closedAuctionEntities = new ArrayList<>();
                System.out.println(numTotalClosedAuctionEntities);
                for (int currentClosedAuctionEntity = 0; currentClosedAuctionEntity < numTotalClosedAuctionEntities; currentClosedAuctionEntity++) {
                    JSONObject j = new JSONObject(receivedRequestReplyObjectInput.readObject().toString());
                    System.out.println(j.toString());
                    closedAuctionEntities.add(j);
                }

                return closedAuctionEntities;

            }

        } catch (IOException | ClassNotFoundException listAllClosedAuctionsException) {

            System.out.println("Exception in Listing All Current Closed Auctions: " +
            		listAllClosedAuctionsException.getMessage());

        }

        return null;

    }
    
    protected Iterable<JSONObject> invokeListAllBidsFromAuction(Long auctionID) {

        try
                (
                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)
                ) {

            requestToSendObjectOutput.writeObject("LIST_ALL_BIDS_FROM_AUCTION");
            requestToSendObjectOutput.writeObject(auctionID);

            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            byte[] requestReply = this.serviceProxy.invokeUnordered(requestToSendByteArrayOutputStream.toByteArray());

            if (requestReply.length == 0) {

                return null;

            }

            try
                    (
                            ByteArrayInputStream receivedRequestReplyByteArrayInputStream =
                                    new ByteArrayInputStream(requestReply);

                            ObjectInput receivedRequestReplyObjectInput =
                                    new ObjectInputStream(receivedRequestReplyByteArrayInputStream)
                    ) {

                int numTotalBidsFromAuctionBankEntities = (int) receivedRequestReplyObjectInput.readObject();

                List<JSONObject> bidEntitiesFromAuctionEntity = new ArrayList<>();
                System.out.println(numTotalBidsFromAuctionBankEntities);
                for (int currentBidEntity = 0; currentBidEntity < numTotalBidsFromAuctionBankEntities; currentBidEntity++) {
                    JSONObject j = new JSONObject(receivedRequestReplyObjectInput.readObject().toString());
                    System.out.println(j.toString());
                    bidEntitiesFromAuctionEntity.add(j);
                }

                return bidEntitiesFromAuctionEntity;

            }

        } catch (IOException | ClassNotFoundException listAllBidsFromAuctionException) {

            System.out.println("Exception in Listing All Bids from an Auction: " +
            		listAllBidsFromAuctionException.getMessage());

        }

        return null;

    }
    


    protected Iterable<JSONObject> invokeListAllBidsFromUser(String who) {

        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("LIST_ALL_BIDS_FROM_USER");
            requestToSendObjectOutput.writeObject(who);

            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            byte[] requestReply = this.serviceProxy.invokeUnordered(requestToSendByteArrayOutputStream.toByteArray());

            if (requestReply.length == 0) {

                return null;

            }

            try
                    (
                            ByteArrayInputStream receivedRequestReplyByteArrayInputStream =
                                    new ByteArrayInputStream(requestReply);

                            ObjectInput receivedRequestReplyObjectInput =
                                    new ObjectInputStream(receivedRequestReplyByteArrayInputStream)
                    ) {

                int numTotalBidsFromUserBankEntities = (int) receivedRequestReplyObjectInput.readObject();

                List<JSONObject> bidEntitiesFromUserBankEntity = new ArrayList<>();
                System.out.println(numTotalBidsFromUserBankEntities);
                for (int currentBidEntity = 0; currentBidEntity < numTotalBidsFromUserBankEntities; currentBidEntity++) {
                    JSONObject j = new JSONObject(receivedRequestReplyObjectInput.readObject().toString());
                    System.out.println(j.toString());
                    bidEntitiesFromUserBankEntity.add(j);
                }

                return bidEntitiesFromUserBankEntity;

            }

        } catch (IOException | ClassNotFoundException listAllBidsFromUserException) {

            System.out.println("Exception in Listing All Bids from an User: " +
            		listAllBidsFromUserException.getMessage());

        }

        return null;

    }
    
    protected BidEntity invokeCheckWinnerBidClosedAuction(Long auctionID) {

        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("CHECK_WINNER_BID_CLOSED_AUCTION");
            requestToSendObjectOutput.writeObject(auctionID);

            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            byte[] requestReply = this.serviceProxy.invokeUnordered(requestToSendByteArrayOutputStream.toByteArray());

            if (requestReply.length == 0) {

                return null;

            }

            try
                    (
                            ByteArrayInputStream receivedRequestReplyByteArrayInputStream =
                                    new ByteArrayInputStream(requestReply);

                            ObjectInput receivedRequestReplyObjectInput =
                                    new ObjectInputStream(receivedRequestReplyByteArrayInputStream)
                    ) {

                JSONObject j = new JSONObject(receivedRequestReplyObjectInput.readObject().toString());
                return new BidEntity(j.getLong("id"),j.getString("username"),j.getLong("amount"),j.getLong("timestamp"));

            }

        } catch (IOException | ClassNotFoundException findingAnUserException) {

            System.out.println("Exception in finding an User/Client: " +
                    findingAnUserException.getMessage());

        }

        return null;
        
    }

    protected JSONObject invokeCreateSmartContract(String who,byte[] code){
        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("CREATE_SMART_CONTRACT");
            requestToSendObjectOutput.writeObject(who);
            requestToSendObjectOutput.writeInt(code.length);
            requestToSendObjectOutput.write(code);
            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            byte[] requestReply = this.serviceProxy.invokeOrdered(requestToSendByteArrayOutputStream.toByteArray());

            if (requestReply.length == 0) {

                return null;

            }

            try
                    (
                            ByteArrayInputStream receivedRequestReplyByteArrayInputStream =
                                    new ByteArrayInputStream(requestReply);

                            ObjectInput receivedRequestReplyObjectInput =
                                    new ObjectInputStream(receivedRequestReplyByteArrayInputStream)
                    ) {

                return new JSONObject(receivedRequestReplyObjectInput.readObject().toString());

            }

        } catch (IOException | ClassNotFoundException createMoneyException) {

            System.out.println("Exception in creation of Money by User/Client: " +
                    createMoneyException.getMessage());

        }

        return null;
    }
    
    protected void terminateClientRequestHandlerSession() {
        this.serviceProxy.close();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BankEntity e = invokeFindUser(username);
        return new MyUserDetails(username,e == null? Optional.empty():Optional.of(e));
    }

    private Comparator<byte[]> replyComparator = new Comparator<byte[]>() {

        @Override
        public int compare(byte[] o1, byte[] o2) {

            byte[] o1Content = new byte[o1.length - 20];
            byte[] o2Content = new byte[o2.length - 20];

            System.arraycopy(o1, 0, o1Content, 0, (o1.length - 20) );
            System.arraycopy(o2, 0, o2Content, 0, (o2.length - 20) );

            return (Arrays.equals(o1Content, o2Content) == true) ? 1 : 0;

        }

    };

    private Extractor replyExtractor = new Extractor() {

        @Override
        public TOMMessage extractResponse(TOMMessage[] replies, int sameContent, int lastReceived) {

            int numValidReplies = 0;

            for(TOMMessage reply : replies) {

                byte[] replyBytes = reply.getContent();

                //byte[] replyBytes = TOMMessage.messageToBytes(reply);

                try {

                    PublicKey publicKey = RSAKeyUtils.getPublicKeyFromString("config/keys/publickey" + serviceProxy.getProcessId());

                    byte[] replyContentBytes = new byte[(replyBytes.length - 20)];
                    byte[] replySignatureBytes = new byte[20];

                    System.arraycopy(replyBytes, 0, replyContentBytes, 0, replyContentBytes.length);
                    System.arraycopy(replyBytes, (replyBytes.length - 20), replySignatureBytes, 0, 20);

                    //Signature signature = Signature.getInstance("SHA256withRSA");

                    //signature.initVerify(publicKey);

                    boolean isReplyValid = TOMUtil.verifySignature(publicKey, replyContentBytes, replySignatureBytes);

                    if(isReplyValid) {

                        numValidReplies++;

                    }

                }
                catch (Exception exception) {

                    exception.printStackTrace();

                }

            }

            int fPlus1Consensus = ( serviceProxy.getViewManager().getCurrentView().getF() + 1 );

            if(numValidReplies >= fPlus1Consensus) {

                TOMMessage lastReply = replies[lastReceived];

                byte[] lastReplyBytes = lastReply.getContent();

                byte[] lastReplyContent = new byte[lastReplyBytes.length - 32];

                System.arraycopy(lastReplyBytes, 0, lastReplyContent, 0, (lastReplyBytes.length - 32) );

                return new TOMMessage(lastReply.getSender(), lastReply.getSession(), replies[lastReceived].getSequence(), replies[lastReceived].getOperationId(),
                        lastReplyContent, replies[lastReceived].getViewID(), replies[lastReceived].getReqType());

            }
            else {

                System.err.println("Invalid Consensus Reply: The Consensus wasn't obtained by at least f+1 Replicas : " +  fPlus1Consensus);

                return null;

            }

        }

    };

}
