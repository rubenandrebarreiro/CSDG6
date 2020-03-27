package fct.unl.pt.csdw1.Repositories;

import fct.unl.pt.csdw1.Entities.BankEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BankRepo extends CrudRepository<BankEntity, Long> {
    Optional<BankEntity> findByOwnerName(String ownerName);
}
