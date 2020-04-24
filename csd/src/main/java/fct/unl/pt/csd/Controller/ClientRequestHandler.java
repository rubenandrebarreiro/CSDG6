package fct.unl.pt.csd.Controller;

import bftsmart.tom.ServiceProxy;
import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Enumerations.BankServiceRequestType;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRequestHandler {

    private String username;

    private ServiceProxy serviceProxy;

    public ClientRequestHandler(String username) {

        this.username = username;
        this.serviceProxy = new ServiceProxy(username.hashCode()); // TODO ID Client??

    }

    public BankEntity invokeCreateNew(String username, String password, Long amount) {

        try
        (

                ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

        )
        {

            requestToSendObjectOutput.writeObject(BankServiceRequestType.REGISTER_USER);
            requestToSendObjectOutput.writeObject(username);
            requestToSendObjectOutput.writeObject(password);
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
            )
            {

                return (BankEntity) receivedRequestReplyObjectInput.readObject();

            }

        }
        catch (IOException | ClassNotFoundException registerUserException) {

            System.out.println("Exception in registration of a New User/Client: " +
                               registerUserException.getMessage());

        }

        return null;

    }

    public JSONObject invokeCreateMoney(String who, Long amount) {

        try
        (

                ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

        )
        {

            requestToSendObjectOutput.writeObject(BankServiceRequestType.CREATE_MONEY);
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
                    )
            {

                return (JSONObject) receivedRequestReplyObjectInput.readObject();

            }

        }
        catch (IOException | ClassNotFoundException createMoneyException) {

            System.out.println("Exception in creation of Money by User/Client: " +
                               createMoneyException.getMessage());

        }

        return null;

    }

    public JSONObject invokeTransferMoney(String from, String to, Long amount) {

        try
        (

                ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

        )
        {

            requestToSendObjectOutput.writeObject(BankServiceRequestType.TRANSFER_MONEY);
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
                    )
            {

                return (JSONObject) receivedRequestReplyObjectInput.readObject();

            }

        }
        catch (IOException | ClassNotFoundException transferMoneyException) {

            System.out.println("Exception in Transfer Money From an User/Client to another User/Client: " +
                               transferMoneyException.getMessage());

        }

        return null;

    }

    public BankEntity invokeFindUser(String username) {

        try
        (

                ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

        )
        {

            requestToSendObjectOutput.writeObject(BankServiceRequestType.FIND_USER);
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
            )
            {

                return (BankEntity) receivedRequestReplyObjectInput.readObject();

            }

        }
        catch (IOException | ClassNotFoundException registerUserException) {

            System.out.println("Exception in registration of a New User/Client: " +
                    registerUserException.getMessage());

        }

        return null;

    }

    public Iterable<BankEntity> invokeListAllBankAccounts() {

        try
        (

                ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

        )
        {

            requestToSendObjectOutput.writeObject(BankServiceRequestType.LIST_ALL_BANK_ACCOUNTS);

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
            )
            {

                int numTotalUserBankEntities = (int) receivedRequestReplyObjectInput.readObject();

                List<BankEntity> usersBankEntities = new ArrayList<>();

                for(int currentUserBankEntity = 0; currentUserBankEntity < numTotalUserBankEntities; currentUserBankEntity++) {

                    usersBankEntities.add((BankEntity) receivedRequestReplyObjectInput.readObject());

                }

                return usersBankEntities;

            }

        }
        catch (IOException | ClassNotFoundException listAllBankAccountsException) {

            System.out.println("Exception in Listing All Bank Accounts: " +
                               listAllBankAccountsException.getMessage());

        }

        return null;

    }

    public Long invokeCheckCurrentAmount(String username) {

        try
        (

                ByteArrayOutputStream requestToSendByteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutput requestToSendObjectOutput = new ObjectOutputStream(requestToSendByteArrayOutputStream)

        )
        {

            requestToSendObjectOutput.writeObject(BankServiceRequestType.CHECK_CURRENT_AMOUNT);
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
            )
            {

                return (long) receivedRequestReplyObjectInput.readObject();

            }

        }
        catch (IOException | ClassNotFoundException registerUserException) {

            System.out.println("Exception in registration of a New User/Client: " +
                    registerUserException.getMessage());

        }

        return null;

    }

    public void terminateClientRequestHandlerSession() {

        this.serviceProxy.close();

    }

}
