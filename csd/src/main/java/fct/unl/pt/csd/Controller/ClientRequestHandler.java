package fct.unl.pt.csd.Controller;

import bftsmart.tom.ServiceProxy;
import fct.unl.pt.csd.Entities.BankEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientRequestHandler implements UserDetailsService {

    private String username;

    private ServiceProxy serviceProxy;

    @Autowired
    public ClientRequestHandler() {
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

    protected JSONObject invokeCreateMoney(String who, Long intitialAmount, Long finalAmount) {
        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("CREATE_MONEY");
            requestToSendObjectOutput.writeObject(who);
            requestToSendObjectOutput.writeObject(intitialAmount);
            requestToSendObjectOutput.writeObject(finalAmount);
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

    protected JSONObject invokeTransferMoney(String from, long fromSaldo, String to, long toSaldo, long fromAmount, long toAmount ) {

        try
                (

                        ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

                ) {

            requestToSendObjectOutput.writeObject("TRANSFER_MONEY");
            requestToSendObjectOutput.writeObject(from);
            requestToSendObjectOutput.writeObject(fromSaldo);
            requestToSendObjectOutput.writeObject(to);
            requestToSendObjectOutput.writeObject(toSaldo);
            requestToSendObjectOutput.writeObject(fromAmount);
            requestToSendObjectOutput.writeObject(toSaldo);
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
                    int numTotalUserBankEntities = (int) receivedRequestReplyObjectInput.readObject();

                    JSONArray arr = new JSONArray(receivedRequestReplyObjectInput.readObject());

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
