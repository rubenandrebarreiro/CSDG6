package fct.unl.pt.csdw1.Services;

import fct.unl.pt.csdw1.Entities.BankEntity;
import fct.unl.pt.csdw1.Repositories.BankRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankService {
    private final BankRepo bankRepo;

    @Autowired
    public BankService(final BankRepo bankRepo) {
        this.bankRepo = bankRepo;
    }

    public BankEntity createUser(String ownerName, Long amount){
        return bankRepo.save(new BankEntity(ownerName, amount));
    }

    public Iterable<BankEntity> getAllBankAcc(){
        return bankRepo.findAll();
    }

}
