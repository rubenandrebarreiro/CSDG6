import org.json.JSONObject;

import java.util.Optional;

public class BankServiceHelper {

    protected static BankEntity registerUser(String username, String password, Long amount, BankRepository bankRepo) {

        return bankRepo.save(new BankEntity(username, password, amount));
    }

    protected static BankEntity findUser(String username, BankRepository bankRepo) {
            return bankRepo.findByUserName(username).get();
    }

    protected static Iterable<BankEntity> getAllBankAcc(BankRepository bankRepo) {
        return bankRepo.findAll();

    }

    protected static JSONObject transferMoney(String from, long fromAmount, String to, long toAmount, BankRepository bankRepo) {
        Optional<BankEntity> beFrom = bankRepo.findByUserName(from);
        Optional<BankEntity> beTo = bankRepo.findByUserName(to);

        BankEntity b = beFrom.get();

        b.updateAmount(fromAmount);

        bankRepo.save(b);
        b = beTo.get();

        b.updateAmount(toAmount);
        bankRepo.save(b);

        return new JSONObject().put("Success", "True");
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

}