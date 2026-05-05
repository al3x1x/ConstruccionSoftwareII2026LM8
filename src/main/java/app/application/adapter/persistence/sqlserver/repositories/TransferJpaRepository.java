package app.application.adapter.persistence.sqlserver.repositories;

import app.application.adapter.persistence.sqlserver.entities.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransferJpaRepository extends JpaRepository<TransferEntity, String> {
    List<TransferEntity> findByOriginAccount(String originAccount);
    List<TransferEntity> findByDestinationAccount(String destinationAccount);
}
