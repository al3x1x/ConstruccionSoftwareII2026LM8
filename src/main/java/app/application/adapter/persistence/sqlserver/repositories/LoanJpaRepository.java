package app.application.adapter.persistence.sqlserver.repositories;

import app.application.adapter.persistence.sqlserver.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoanJpaRepository extends JpaRepository<LoanEntity, String> {
    List<LoanEntity> findByClientId(String clientId);
}
