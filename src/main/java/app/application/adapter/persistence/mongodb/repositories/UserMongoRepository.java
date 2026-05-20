package app.application.adapter.persistence.mongodb.repositories;

import app.application.adapter.persistence.mongodb.documents.UserDocument;
import app.domain.enums.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface UserMongoRepository extends MongoRepository<UserDocument, String> {
    List<UserDocument> findByRole(UserRole role);
    Optional<UserDocument> findByUsername(String username);
}

