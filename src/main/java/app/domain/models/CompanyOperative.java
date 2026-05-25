package app.domain.models;

import app.domain.enums.UserRole;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("COMPANY_OPERATIVE")
public class CompanyOperative extends User {

    @Column(nullable = false)
    private String companyId;

    // ── Constructor ───────────────────────────────────────────────────

    public CompanyOperative(String userId, String fullName, String identificationNumber,
                             String email, String phone, LocalDate birthDate,
                             String address, String username, String passwordHash,
                             String companyId) {
        super(userId, fullName, identificationNumber, email, phone,
              birthDate, address, UserRole.COMPANY_OPERATIVE, username, passwordHash);

        this.companyId = companyId;
    }

    // ── Métodos de negocio ────────────────────────────────────────────

    public Transfer createTransfer(String transferId, String originAccount,
                                    String destinationAccount, BigDecimal amount) {
        return new Transfer(transferId, originAccount, destinationAccount,
                            amount, this.getUserId());
    }

    public List<Transfer> createBulkPayment(String originAccount, List<String[]> entries) {
        List<Transfer> transfers = new ArrayList<>();
        for (String[] entry : entries) {
            String destinationAccount = entry[0];
            BigDecimal amount = new BigDecimal(entry[1]);
            String transferId = entry[2];
            transfers.add(createTransfer(transferId, originAccount, destinationAccount, amount));
        }
        return transfers;
    }

    // ── Getters & Setters ─────────────────────────────────────────────

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    @Override
    public String toString() {
        return "CompanyOperative{userId='" + getUserId() + "', name='" + getFullName() + "', companyId='" + companyId + "'}";
    }
}