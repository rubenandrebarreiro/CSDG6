package bftProxyServer;
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

    protected static JSONObject transferMoney(String from, String to, long amount, BankRepository bankRepo) {
        Optional<BankEntity> beFrom = bankRepo.findByUserName(from);

        if (beFrom.isPresent()) {

            Optional<BankEntity> beTo = bankRepo.findByUserName(to);

            if (beTo.isPresent()) {

                if (amount > 0) {

                    BankEntity b = beFrom.get();

                    if ((b.getAmount() - amount) >= 0) {

                        b.updateAmount(-amount);

                        bankRepo.save(b);
                        b = beTo.get();

                        b.updateAmount(amount);
                        bankRepo.save(b);

                        return new JSONObject().put("Success", "True");

                    } else {

                        return new JSONObject().put("error", "From account doesn't have enough money").put("errorID", 3);

                    }
                } else {

                    return new JSONObject().put("error", "Amount<0").put("errorID", 2);

                }

            } else {

                return new JSONObject().put("error", "To account doesn't exist").put("errorID", 1);

            }

        } else {

            return new JSONObject().put("error", "From account doesn't exist").put("errorID", 0);

        }

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