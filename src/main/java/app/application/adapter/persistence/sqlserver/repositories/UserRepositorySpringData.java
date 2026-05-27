package app.application.adapter.persistence.sqlserver.repositories;

import app.domain.models.User;
import app.domain.ports.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepositorySpringData extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByIdentificationNumber(String identificationNumber);

    boolean existsByEmail(String email);
    boolean existsByIdentificationNumber(String identificationNumber);
}

@Repository
@Primary
class UserRepositoryImpl implements UserRepository {
    private final UserRepositorySpringData springDataRepo;

    public UserRepositoryImpl(UserRepositorySpringData springDataRepo) {
        this.springDataRepo = springDataRepo;
    }

    @Override
    public Optional<User> findById(String id) {
        return springDataRepo.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return springDataRepo.findByUsername(username);
    }

    @Override
    public void save(User user) {
        springDataRepo.save(user);
    }

    @Override
    public void update(User user) {
        springDataRepo.save(user);
    }

    @Override
    public boolean existsById(String id) {
        return springDataRepo.existsById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return springDataRepo.findByUsername(username).isPresent();
    }

    // AGREGADO: Conexión con las validaciones requeridas por el dominio
    @Override
    public boolean existsByEmail(String email) {
        return springDataRepo.existsByEmail(email);
    }

    @Override
    public boolean existsByIdentificationNumber(String idNum) {
        return springDataRepo.existsByIdentificationNumber(idNum);
    }
}