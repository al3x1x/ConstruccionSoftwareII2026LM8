package app.domain.models;

import app.domain.enums.UserRole;

public class UserFactory {

    public static User create(UserRole role) {

        return switch (role) {

            case INTERNAL_ANALYST -> new InternalAnalyst();

            case COMPANY_CLIENT -> new CompanyClient();

            case NATURAL_PERSON_CLIENT -> new NaturalPersonClient();

            case TELLER_EMPLOYEE -> new TellerEmployee();

            case COMMERCIAL_EMPLOYEE -> new CommercialEmployee();

            case COMPANY_SUPERVISOR -> new CompanySupervisor();

            default -> throw new IllegalArgumentException("Rol no soportado: " + role);
        };
    }
}