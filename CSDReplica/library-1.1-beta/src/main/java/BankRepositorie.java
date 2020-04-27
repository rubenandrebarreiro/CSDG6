import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class BankRepositorie {

    private Map<String, BankEntity> users;

    public BankRepositorie() {
        this.users = new HashMap<String, BankEntity>();
    }

    public Optional<BankEntity> findByUserName(String userName) {
        return Optional.of(users.get(userName));
    }

    public BankEntity save(BankEntity b) {
        return users.put(b.getOwnerName(),b);
    }

    // Function to get the Spliterator
    public static <T> Iterable<T>
    getIterableFromIterator(Iterator<T> iterator)
    {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator()
            {
                return iterator;
            }
        };
    }

    public Iterable<BankEntity> findAll(){
        return getIterableFromIterator(this.users.values().iterator());
    }

}
