package fct.unl.pt.csdw1.Repositories;

import fct.unl.pt.csdw1.Entities.BankEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepo extends CrudRepository<BankEntity, Long> {
    Optional<BankEntity> findByUserName(String userName);
}
