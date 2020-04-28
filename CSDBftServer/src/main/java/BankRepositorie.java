import javax.swing.text.html.Option;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class BankRepositorie implements Serializable {

    private Map<String, BankEntity> users;

    public BankRepositorie() {
        this.users = new HashMap<String, BankEntity>();
    }

    public Optional<BankEntity> findByUserName(String userName) {
        BankEntity b = users.get(userName);
        return b == null? Optional.empty() : Optional.of(users.get(userName));
    }

    public BankEntity save(BankEntity b) {
        users.put(b.getOwnerName(),b);
        return users.get(b.getOwnerName());
    }

    // Function to get the Spliterator
    public static<T> Iterable<T> iteratorToIterable(Iterator<T> iterator) {
        return () -> iterator;
    }

    public Iterable<BankEntity> findAll(){
        return iteratorToIterable(this.users.values().iterator());
    }

    public Iterator<BankEntity> iterator(){
        return this.users.values().iterator();
    }

    public int getSize(){
        return this.users.size();
    }

    protected void save(int id){
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("/users"+id+".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(users);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in /users"+id+".ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    protected void load(int id){
        try {
            FileInputStream fileIn = new FileInputStream("/users"+id+".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.users = (HashMap<String,BankEntity>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            return;
        }
    }
}
