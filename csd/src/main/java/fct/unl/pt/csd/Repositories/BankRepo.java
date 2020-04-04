package fct.unl.pt.csd.Repositories;

import fct.unl.pt.csd.Entities.BankEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepo extends CrudRepository<BankEntity, Long> {
    Optional<BankEntity> findByUserName(String userName);
}
