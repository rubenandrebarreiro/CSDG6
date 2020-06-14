package fct.unl.pt.csd.Controller;

import bftsmart.reconfiguration.util.RSAKeyUtils;
import bftsmart.tom.ServiceProxy;
import bftsmart.tom.core.messages.TOMMessage;
import bftsmart.tom.util.Extractor;
import bftsmart.tom.util.TOMUtil;
import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Security.MyUserDetails;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
        
        Comparator<byte[]> replyComparator = new Comparator<byte[]>() {

			@Override
			public int compare(byte[] o1, byte[] o2) {

				byte[] o1Content = new byte[o1.length - 20];
				byte[] o2Content = new byte[o2.length - 20];
				
				System.arraycopy(o1, 0, o1Content, 0, (o1.length - 20) );
				System.arraycopy(o2, 0, o2Content, 0, (o2.length - 20) );
				
				return (Arrays.equals(o1Content, o2Content) == true) ? 1 : 0;
				
			}
			
		};
		
        Extractor replyExtractor = new Extractor() {
			
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
		
        
        this.serviceProxy = new ServiceProxy(1014, "config/system.config", "config/hosts.config", "config/keys/", replyComparator, replyExtractor);
        
        
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
                return new BankEntity(j.getString("username"),j.getString("password"),j.getLong("amount"));

            }

        } catch (IOException | ClassNotFoundException registerUserException) {

            System.out.println("Exception in registration of a New User/Client: " +
                    registerUserException.getMessage());

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

        } catch (IOException | ClassNotFoundException registerUserException) {

            System.out.println("Exception in registration of a New User/Client: " +
                    registerUserException.getMessage());

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
