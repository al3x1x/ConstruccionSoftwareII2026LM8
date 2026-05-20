package app.application.adapter.persistence.mongodb;

import app.application.adapter.persistence.mongodb.documents.UserDocument;
import app.application.adapter.persistence.mongodb.repositories.UserMongoRepository;
import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.ports.UserRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MongoDbUserRepository implements UserRepository {
    private final UserMongoRepository mongoRepository;

    public MongoDbUserRepository(UserMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public User findById(String id) {
        return mongoRepository.findById(id)
                .map(this::toDomainModel)
                .orElse(null);
    }

    @Override
    public void save(User user) {
        UserDocument document = toDocument(user);
        mongoRepository.save(document);
    }

    @Override
    public List<User> findByRole(UserRole role) {
        return mongoRepository.findByRole(role)
                .stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAll() {
        return mongoRepository.findAll()
                .stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public void update(User user) {
        save(user);
    }

    @Override
    public boolean existsById(String id) {
        return mongoRepository.existsById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return mongoRepository.findByUsername(username)
                .map(this::toDomainModel);
    }

    private User toDomainModel(UserDocument document) {
        // No se puede instanciar User directamente (es abstracta)
        // Este repositorio solo actúa como almacenamiento
        // El controlador debe usar subclases concretas (NaturalPersonClient, etc.)
        return null; // Será sobrescrito en controlador
    }

    private UserDocument toDocument(User user) {
        UserDocument document = new UserDocument();
        document.setId(user.getUserId());
        document.setName(user.getFullName());
        document.setUsername(user.getUsername());
        document.setEmail(user.getEmail());
        document.setPhone(user.getPhone());
        document.setRole(user.getRole());
        document.setPasswordHash(user.getPasswordHash());
        document.setStatus(user.getStatus());
        document.setUpdatedAt(java.time.LocalDate.now());
        return document;
    }
}

