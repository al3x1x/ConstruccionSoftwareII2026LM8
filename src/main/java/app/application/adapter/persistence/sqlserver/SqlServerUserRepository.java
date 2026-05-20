package app.application.adapter.persistence.sqlserver;

import org.springframework.stereotype.Component;
import app.domain.models.User;
import app.domain.models.NaturalPersonClient;
import app.domain.models.CommercialEmployee;
import app.domain.ports.UserRepository;
import app.application.adapter.persistence.sqlserver.repositories.UserJpaRepository;
import app.application.adapter.persistence.sqlserver.entities.UserEntity;
import app.domain.enums.UserRole;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SqlServerUserRepository implements UserRepository {
    private final UserJpaRepository jpaRepository;

    public SqlServerUserRepository(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findById(String id) {
        return jpaRepository.findById(id).map(this::toDomainModel);
    }

    @Override
    public void save(User user) {
        jpaRepository.save(toJpaEntity(user));
    }

    @Override
    public void update(User user) {
        save(user);
    }

    @Override
    public boolean existsById(String id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<User> findByRole(UserRole role) {
        return jpaRepository.findByRole(role).stream()
            .map(this::toDomainModel)
            .collect(Collectors.toList());
    }

    private User toDomainModel(UserEntity entity) {
        if (UserRole.NATURAL_PERSON_CLIENT.equals(entity.getRole())) {
            NaturalPersonClient client = new NaturalPersonClient(
                entity.getUserId(),
                entity.getFullName(),
                entity.getIdentificationNumber(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getBirthDate(),
                entity.getAddress(),
                entity.getUsername(),
                entity.getPasswordHash()
            );
            client.setAssignedCommercialEmployeeId(entity.getAssignedCommercialEmployeeId());
            client.setStatus(entity.getStatus());
            return client;
        } else if (UserRole.COMMERCIAL_EMPLOYEE.equals(entity.getRole())) {
            return new CommercialEmployee(
                entity.getUserId(),
                entity.getFullName(),
                entity.getIdentificationNumber(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getBirthDate(),
                entity.getAddress(),
                entity.getUsername(),
                entity.getPasswordHash()
            );
        }
        return null;
    }

    private UserEntity toJpaEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setUserId(user.getUserId());
        entity.setFullName(user.getFullName());
        entity.setIdentificationNumber(user.getIdentificationNumber());
        entity.setEmail(user.getEmail());
        entity.setPhone(user.getPhone());
        entity.setBirthDate(user.getBirthDate());
        entity.setAddress(user.getAddress());
        entity.setRole(user.getRole());
        entity.setStatus(user.getStatus());
        entity.setUsername(user.getUsername());
        entity.setPasswordHash(user.getPasswordHash());

        if (user instanceof NaturalPersonClient) {
            NaturalPersonClient client = (NaturalPersonClient) user;
            entity.setAssignedCommercialEmployeeId(client.getAssignedCommercialEmployeeId());
        }

        return entity;
    }
}
