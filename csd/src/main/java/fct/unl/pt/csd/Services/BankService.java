package fct.unl.pt.csd.Services;

import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Repositories.BankRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankService {
    private final BankRepo bankRepo;

    @Autowired
    public BankService(final BankRepo bankRepo) {
        this.bankRepo = bankRepo;
    }

    public BankEntity registerUser(String userName, String password, Long amount){
        if(!bankRepo.findByUserName(userName).isPresent()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            return bankRepo.save(new BankEntity(userName, passwordEncoder.encode(password), amount));
        }
        else
            return null;
    }

    public Iterable<BankEntity> getAllBankAcc(){
        return bankRepo.findAll();
    }

    public JSONObject transferMoney(String from, String to, long amount){
        Optional<BankEntity> beFrom = bankRepo.findByUserName(from);
        if(beFrom.isPresent()){
            Optional<BankEntity> beTo = bankRepo.findByUserName(to);
            if(beTo.isPresent()){
                if(amount>0){
                    BankEntity b = beFrom.get();
                    if(b.getAmount()-amount>=0) {
                        b.updateAmount(-amount);
                        bankRepo.save(b);
                        b = beTo.get();
                        b.updateAmount(amount);
                        bankRepo.save(b);
                        return new JSONObject().put("Success", "True");
                    }else
                        return new JSONObject().put("error", "From account doesn't have enough money").put("errorID",3);
                }else{
                    return new JSONObject().put("error", "Amount<0").put("errorID",2);
                }
            }else{
                return new JSONObject().put("error", "To account doesn't exist").put("errorID",1);
            }
        }else{
            return new JSONObject().put("error", "From account doesn't exist").put("errorID",0);
        }
    }

    public JSONObject createMoney(String who,long amount){
        Optional<BankEntity> be = bankRepo.findByUserName(who);
        if(be.isPresent()){
            BankEntity b = be.get();
            b.updateAmount(b.getAmount()+amount);
            bankRepo.save(b);
            return new JSONObject().put("Success","True").put("amount",b.getAmount());
        }else
            return new JSONObject().put("error","User not found "+who);
    }

    public long currentAmount(String who){
        Optional<BankEntity> be = bankRepo.findByUserName(who);
        if(be.isPresent()){
            BankEntity b = be.get();
            return b.getAmount();
        }else
            return -1;
    }

}
