package app.application.adapter.persistence;

import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.ports.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryUserRepository implements UserRepository {
    private Map<String, User> users = new HashMap<>();

    @Override
    public User findById(String id) {
        return users.get(id);
    }

    @Override
    public void save(User user) {
        users.put(user.getUserId(), user);
    }

    @Override
    public List<User> findByRole(UserRole role) {
        return users.values().stream()
            .filter(u -> role.equals(u.getRole()))
            .collect(Collectors.toList());
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public void update(User user) {
        users.put(user.getUserId(), user);
    }

    @Override
    public boolean existsById(String id) {
        return users.containsKey(id);
    }
}
