package app.domain.services;

import app.domain.ports.UserRepository;
import app.domain.services.commands.CreateCompanyClientCommand;

public class CreatePatient {
    private UserRepository userRepository;

    public CreatePatient(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(CreateCompanyClientCommand command) {
        // Validate client doesn't already exist
        if (userRepository.existsById(command.getClientId())) {
            throw new IllegalArgumentException("Company client with ID " + command.getClientId() + " already exists");
        }

        // Note: Company client creation would involve creating a CompanyClient entity
        // (similar to NaturalPersonClient but for company entities)
        // This service validates business rules for company client creation
    }
}
