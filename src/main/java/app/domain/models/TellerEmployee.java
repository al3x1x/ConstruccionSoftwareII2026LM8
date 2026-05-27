package app.domain.models;

import app.domain.enums.UserRole;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("TELLER_EMPLOYEE")
public class TellerEmployee extends User {

    // ── Constructor Vacío Obligatorio ─────────────────────────────────
    public TellerEmployee() {
        super();
    }

    // ── Constructor Parametrizado ─────────────────────────────────────
    public TellerEmployee(String userId, String fullName, String identificationNumber,
                          String email, String phone, LocalDate birthDate,
                          String address, String username, String passwordHash) {
        super(userId, fullName, identificationNumber, email, phone,
              birthDate, address, UserRole.TELLER_EMPLOYEE, username, passwordHash);
    }
}