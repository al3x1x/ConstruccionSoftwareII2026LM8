package app.domain.models;

import app.domain.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

public class CompanyClient extends User {

    private String companyName;
    private String taxId;
    private String legalRepresentativeId;
    private List<BankAccount> accounts;
    private List<Loan> loans;
    private List<User> operativeUsers;

    // ── Constructor vacío obligatorio para Hibernate ───────────────────
    public CompanyClient() {
        super();
        this.accounts = new ArrayList<>();
        this.loans = new ArrayList<>();
        this.operativeUsers = new ArrayList<>();
    }

    // ── Constructor ───────────────────────────────────────────────────

    public CompanyClient(String userId, String companyName, String taxId,
                          String email, String phone, String address,
                          String legalRepresentativeId, String username, String passwordHash) {
        super(userId, companyName, taxId, email, phone,
              null, address, UserRole.COMPANY_CLIENT, username, passwordHash);

        this.companyName = companyName;
        this.taxId = taxId;
        this.legalRepresentativeId = legalRepresentativeId;
        this.accounts = new ArrayList<>();
        this.loans = new ArrayList<>();
        this.operativeUsers = new ArrayList<>();
    }

    // ── Métodos de negocio ────────────────────────────────────────────

    public void addAccount(BankAccount account) {
        this.accounts.add(account);
    }

    public void addLoan(Loan loan) {
        this.loans.add(loan);
    }

    public void addOperativeUser(User user) {
        this.operativeUsers.add(user);
    }

    public void removeOperativeUser(User user) {
        this.operativeUsers.remove(user);
    }

    // ── Getters & Setters ─────────────────────────────────────────────

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }

    public String getLegalRepresentativeId() { return legalRepresentativeId; }
    public void setLegalRepresentativeId(String legalRepresentativeId) { this.legalRepresentativeId = legalRepresentativeId; }

    public List<BankAccount> getAccounts() { return accounts; }
    public List<Loan> getLoans() { return loans; }
    public List<User> getOperativeUsers() { return operativeUsers; }

    @Override
    public String toString() {
        return "CompanyClient{userId='" + getUserId() + "', companyName='" + companyName + "', taxId='" + taxId + "'}";
    }
}