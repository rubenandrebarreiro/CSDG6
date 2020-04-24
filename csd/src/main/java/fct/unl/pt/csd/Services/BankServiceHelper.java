package fct.unl.pt.csd.Services;

import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Repositories.BankRepo;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class BankServiceHelper {

    protected static BankEntity registerUser(String username, String password, Long amount, BankRepo bankRepo) {

        if(!bankRepo.findByUserName(username).isPresent()) {

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            return bankRepo.save(new BankEntity(username, passwordEncoder.encode(password), amount));

        }
        else {

            return null;

        }

    }

    protected static BankEntity findUser(String username, BankRepo bankRepo) {

        if ( bankRepo.findByUserName(username).isPresent() ) {

            return bankRepo.findByUserName(username).get();

        }

        return null;

    }

    protected static Iterable<BankEntity> getAllBankAcc(BankRepo bankRepo){

        return bankRepo.findAll();

    }

    protected static JSONObject transferMoney(String from, String to, long amount, BankRepo bankRepo) {
        Optional<BankEntity> beFrom = bankRepo.findByUserName(from);

        if(beFrom.isPresent()){

            Optional<BankEntity> beTo = bankRepo.findByUserName(to);

            if(beTo.isPresent()){

                if(amount > 0) {

                    BankEntity b = beFrom.get();

                    if ( ( b.getAmount() - amount ) >= 0 ) {

                        b.updateAmount(-amount);

                        bankRepo.save(b);
                        b = beTo.get();

                        b.updateAmount(amount);
                        bankRepo.save(b);

                        return new JSONObject().put("Success", "True");

                    }
                    else {

                        return new JSONObject().put("error", "From account doesn't have enough money").put("errorID", 3);

                    }
                }
                else {

                    return new JSONObject().put("error", "Amount<0").put("errorID",2);

                }

            }
            else {

                return new JSONObject().put("error", "To account doesn't exist").put("errorID",1);

            }

        }
        else {

            return new JSONObject().put("error", "From account doesn't exist").put("errorID",0);

        }

    }

    protected static JSONObject createMoney(String who,long amount, BankRepo bankRepo){

        Optional<BankEntity> be = bankRepo.findByUserName(who);

        if( be.isPresent() ) {

            BankEntity b = be.get();

            b.updateAmount(b.getAmount()+amount);
            bankRepo.save(b);

            return new JSONObject().put("Success","True").put("amount",b.getAmount());

        }
        else

            return new JSONObject().put("error","User not found "+who);

    }

    protected static long currentAmount(String who, BankRepo bankRepo) {
        Optional<BankEntity> be = bankRepo.findByUserName(who);
        if (be.isPresent()) {
            BankEntity b = be.get();
            return b.getAmount();
        }
        else {

            return -1;

        }

    }

}
