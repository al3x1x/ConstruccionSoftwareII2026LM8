package app.domain.ports;

import app.domain.models.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String id);
    
    Optional<User> findByUsername(String username);
    
    void save(User user);
    void update(User user);
    boolean existsById(String id);
    boolean existsByUsername(String username);
}