package app.application.adapter.persistence.sqlserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import app.application.adapter.persistence.sqlserver.entities.UserEntity;
import app.domain.enums.UserRole;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findByRole(UserRole role);
}
