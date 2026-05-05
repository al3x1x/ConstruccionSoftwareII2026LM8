package app.application.adapter.persistence.sqlserver.repositories;

import app.application.adapter.persistence.sqlserver.entities.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BankAccountJpaRepository extends JpaRepository<BankAccountEntity, String> {
    List<BankAccountEntity> findByHolderId(String holderId);
}
