package app.domain.models;

import app.domain.enums.UserRole;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("COMMERCIAL_EMPLOYEE")
public class CommercialEmployee extends User {

    // ── Constructor Vacío Obligatorio ─────────────────────────────────
    public CommercialEmployee() {
        super();
    }

    // ── Constructor Parametrizado ─────────────────────────────────────
    public CommercialEmployee(String userId, String fullName, String identificationNumber,
                             String email, String phone, LocalDate birthDate,
                             String address, String username, String passwordHash) {
        super(userId, fullName, identificationNumber, email, phone,
              birthDate, address, UserRole.COMMERCIAL_EMPLOYEE, username, passwordHash);
    }
}