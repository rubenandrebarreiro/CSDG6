package fct.unl.pt.csd.Services;

import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Repositories.BankRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankServiceHelper {

    private final BankRepo bankRepo;

    @Autowired
    public BankServiceHelper(final BankRepo br){
        this.bankRepo = br ;
    }

    public BankEntity registerUser(String username, String password, Long amount) {

        if(!bankRepo.findByUserName(username).isPresent()) {

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            return bankRepo.save(new BankEntity(username, passwordEncoder.encode(password), amount));

        }
        else {

            return null;

        }

    }

    public BankEntity findUser(String username) {

        if ( bankRepo.findByUserName(username).isPresent() ) {

            return bankRepo.findByUserName(username).get();

        }

        return null;

    }

    public Iterable<BankEntity> getAllBankAcc(){

        return bankRepo.findAll();

    }

    /*public boolean canTransfer(String from, String to, long amount){

        Optional<BankEntity> beFrom = bankRepo.findByUserName(from);

        Optional<BankEntity> beTo = bankRepo.findByUserName(to);

        if ( ( beFrom.isPresent() ) && ( beTo.isPresent() ) ) {

            if ((amount > 0) && ((beFrom.get().getAmount() - amount) >= 0))
                return true;
        }
        return false;
    }*/

    public long[] getTransferInfo(String from, String to, long amount){
        long[] results = new long[4];
        Optional<BankEntity> beFrom = bankRepo.findByUserName(from);
        Optional<BankEntity> beTo = bankRepo.findByUserName(to);
        if ( ( beFrom.isPresent() ) && ( beTo.isPresent() ) ) {
            results[0] = beFrom.get().getAmount();
            results[1] = beTo.get().getAmount();
            results[2] =( beFrom.get().getAmount())-amount;
            results[3] = beFrom.get().getAmount()+amount;
            this.transferMoney(from, to, amount);
            return results;
        }
        return null;
    }

    public void cancelTransfer(String from, String to, long fromAmount, long toAmount){
        Optional<BankEntity> beFrom = bankRepo.findByUserName(from);
        Optional<BankEntity> beTo = bankRepo.findByUserName(to);
        if ( ( beFrom.isPresent() ) && ( beTo.isPresent() ) ) {
            beFrom.get().updateAmount(fromAmount);
            beTo.get().updateAmount(toAmount);
        }
    }

    public JSONObject transferMoney(String from, String to, long amount) {
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

    public JSONObject createMoney(String who,long amount){

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

    public long currentAmount(String who) {
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
