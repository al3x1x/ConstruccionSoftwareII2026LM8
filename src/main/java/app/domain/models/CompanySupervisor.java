package app.domain.models;

import app.domain.enums.TransferStatus;
import app.domain.enums.UserRole;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("COMPANY_SUPERVISOR")
public class CompanySupervisor extends User {

    @Column(nullable = false)
    private String companyId;

    // ── Constructor Vacío Obligatorio para Hibernate ──────────────────
    public CompanySupervisor() {
        super();
    }

    // ── Constructor Parametrizado ─────────────────────────────────────

    public CompanySupervisor(String userId, String fullName, String identificationNumber,
                              String email, String phone, LocalDate birthDate,
                              String address, String username, String passwordHash,
                              String companyId) {
        super(userId, fullName, identificationNumber, email, phone,
              birthDate, address, UserRole.COMPANY_SUPERVISOR, username, passwordHash);

        this.companyId = companyId;
    }

    // ── Métodos de negocio ────────────────────────────────────────────

    public void approveTransfer(Transfer transfer, BankAccount originAccount) {
        if (!TransferStatus.AWAITING_APPROVAL.equals(transfer.getStatus())) {
            throw new IllegalStateException("Only transfers AWAITING_APPROVAL can be approved.");
        }
        if (originAccount.getCurrentBalance().compareTo(transfer.getAmount()) < 0) {
            throw new IllegalStateException("Insufficient funds in origin account.");
        }
        transfer.approve(this.getUserId());
        transfer.execute(originAccount, null);
    }

    public void rejectTransfer(Transfer transfer) {
        if (!TransferStatus.AWAITING_APPROVAL.equals(transfer.getStatus())) {
            throw new IllegalStateException("Only transfers AWAITING_APPROVAL can be rejected.");
        }
        transfer.reject(this.getUserId());
    }

    // ── Getters & Setters ─────────────────────────────────────────────

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    @Override
    public String toString() {
        return "CompanySupervisor{userId='" + getUserId() + "', name='" + getFullName() + "', companyId='" + companyId + "'}";
    }
}