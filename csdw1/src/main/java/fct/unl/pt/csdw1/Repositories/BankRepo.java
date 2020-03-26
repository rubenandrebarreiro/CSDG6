package fct.unl.pt.csdw1.Repositories;

import fct.unl.pt.csdw1.Entities.BankEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BankRepo extends CrudRepository<BankEntity, Long> {
    BankEntity findByOwnerName(String ownerName);
}
