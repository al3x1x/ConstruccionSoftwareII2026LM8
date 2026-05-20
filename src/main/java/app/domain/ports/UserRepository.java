package app.domain.ports;

import app.domain.models.User;
import app.domain.enums.UserRole;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User findById(String id);
    void save(User user);
    List<User> findByRole(UserRole role);
    List<User> findAll();
    void update(User user);
    boolean existsById(String id);
    Optional<User> findByUsername(String username);
}

