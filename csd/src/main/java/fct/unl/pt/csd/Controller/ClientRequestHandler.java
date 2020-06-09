package fct.unl.pt.csd.Controller;

import bftsmart.tom.ServiceProxy;
import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Entities.BidEntity;
import fct.unl.pt.csd.Security.MyUserDetails;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientRequestHandler implements UserDetailsService {

    private String username;

    private ServiceProxy serviceProxy;

    @Autowired
    public ClientRequestHandler() {

        //this.username = username;
        // TODO ID Client??

    }

    protected void setUsername(String username) {
        this.username = username;
        this.serviceProxy = new ServiceProxy(1014, "config");
    }

    protected String invokeCreateNew(String username, String password, Long amount) {

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

            requestToSendObjectOutput.flush();
            requestToSendByteArrayOutputStream.flush();

            byte[] requestReply = this.serviceProxy.invokeOrdered(requestToSendByteArrayOutputStream.toByteArray());

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

    protected JSONObject invokeCreateMoney(String who, Long amount) {
        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("CREATE_MONEY");
            requestToSendObjectOutput.writeObject(who);
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

        } catch (IOException | ClassNotFoundException createMoneyException) {

            System.out.println("Exception in creation of Money by User/Client: " +
                    createMoneyException.getMessage());

        }

        return null;

    }

    protected JSONObject invokeTransferMoney(String from, String to, Long amount) {

        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("TRANSFER_MONEY");
            requestToSendObjectOutput.writeObject(from);
            requestToSendObjectOutput.writeObject(to);
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
                return new BankEntity(j.getString("username"),j.getString("password"),j.getLong("amount"));

            }

        } catch (IOException | ClassNotFoundException findingAnUserException) {

            System.out.println("Exception in finding an User/Client: " +
                    findingAnUserException.getMessage());

        }

        return null;

    }

    protected Iterable<JSONObject> invokeListAllBankAccounts() {

        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("LIST_ALL_BANK_ACCOUNTS");

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

                int numTotalUserBankEntities = (int) receivedRequestReplyObjectInput.readObject();

                List<JSONObject> usersBankEntities = new ArrayList<>();
                System.out.println(numTotalUserBankEntities);
                for (int currentUserBankEntity = 0; currentUserBankEntity < numTotalUserBankEntities; currentUserBankEntity++) {
                    JSONObject j = new JSONObject(receivedRequestReplyObjectInput.readObject().toString());
                    System.out.println(j.toString());
                    usersBankEntities.add(j);
                }

                return usersBankEntities;

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
    
    protected void terminateClientRequestHandlerSession() {

        this.serviceProxy.close();

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BankEntity e = invokeFindUser(username);
        return new MyUserDetails(username,e == null? Optional.empty():Optional.of(e));
    }
}
