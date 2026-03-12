package app.domain.models;

import app.domain.enums.UserRole;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class NaturalPersonClient extends User {

    private List<BankAccount> accounts;
    private List<Loan> loans;
    private List<Transfer> transfers;

    // ── Constructor ───────────────────────────────────────────────────

    public NaturalPersonClient(String userId, String fullName, String identificationNumber,
                                String email, String phone, LocalDate birthDate,
                                String address, String username, String passwordHash) {
        super(userId, fullName, identificationNumber, email, phone,
              birthDate, address, UserRole.NATURAL_PERSON_CLIENT, username, passwordHash);

        this.accounts = new ArrayList<>();
        this.loans = new ArrayList<>();
        this.transfers = new ArrayList<>();
    }

    // ── Métodos de negocio ────────────────────────────────────────────

    public boolean isOfLegalAge() {
        int age = Period.between(this.getBirthDate(), LocalDate.now()).getYears();
        return age >= 18;
    }

    public void addAccount(BankAccount account) {
        this.accounts.add(account);
    }

    public void addLoan(Loan loan) {
        this.loans.add(loan);
    }

    public void addTransfer(Transfer transfer) {
        this.transfers.add(transfer);
    }

    // ── Getters ───────────────────────────────────────────────────────

    public List<BankAccount> getAccounts() { return accounts; }
    public List<Loan> getLoans() { return loans; }
    public List<Transfer> getTransfers() { return transfers; }

    @Override
    public String toString() {
        return "NaturalPersonClient{userId='" + getUserId() + "', name='" + getFullName() + "'}";
    }
}