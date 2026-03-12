package app;

import app.domain.enums.*;
import app.domain.models.*;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== SISTEMA BANCARIO ===\n");

        // ── Crear un cliente persona natural ───────────────────────
        NaturalPersonClient client = new NaturalPersonClient(
                "USR-001",
                "Juan Perez",
                "123456789",
                "juan@email.com",
                "3001234567",
                LocalDate.of(1990, 5, 15),
                "Calle 123",
                "juanp",
                "hashed_password"
        );
        System.out.println("Cliente creado: " + client);
        System.out.println("Es mayor de edad: " + client.isOfLegalAge());

        // ── Crear una cuenta bancaria para el cliente ──────────────
        BankAccount account = new BankAccount(
                "ACC-001",
                AccountType.SAVINGS,
                client.getIdentificationNumber(),
                "COP"
        );
        client.addAccount(account);
        System.out.println("\nCuenta creada: " + account);

        // ── Hacer un deposito ──────────────────────────────────────
        TellerEmployee teller = new TellerEmployee(
                "USR-010",
                "Carlos Lopez",
                "987654321",
                "carlos@banco.com",
                "3009876543",
                LocalDate.of(1985, 3, 20),
                "Av Principal",
                "carlosl",
                "hashed_password"
        );
        teller.deposit(account, 50000.00);
        System.out.println("\nDepósito realizado.");
        System.out.println("Saldo después del depósito: " + account.getCurrentBalance());

        // ── Crear una solicitud de prestamo ────────────────────────
        CommercialEmployee commercial = new CommercialEmployee(
                "USR-011",
                "Ana Torres",
                "111222333",
                "ana@banco.com",
                "3001112233",
                LocalDate.of(1988, 7, 10),
                "Carrera 45",
                "anat",
                "hashed_password"
        );
        Loan loan = commercial.createLoanRequest(
                "LOA-001",
                client.getIdentificationNumber(),
                LoanType.CONSUMER,
                20000.00,
                12
        );
        client.addLoan(loan);
        System.out.println("\nPréstamo solicitado: " + loan);

        // ── Analista aprueba y desembolsa el prestamo ──────────────
        InternalAnalyst analyst = new InternalAnalyst(
                "USR-020",
                "Maria Gomez",
                "444555666",
                "maria@banco.com",
                "3004445566",
                LocalDate.of(1982, 1, 25),
                "Calle 78",
                "mariag",
                "hashed_password"
        );

        // Crear cuenta destino para el desembolso
        BankAccount loanAccount = new BankAccount(
                "ACC-002",
                AccountType.PERSONAL,
                client.getIdentificationNumber(),
                "COP"
        );
        client.addAccount(loanAccount);

        analyst.approveLoan(loan, 18000.00, 12.5);
        System.out.println("\nPréstamo aprobado: " + loan);

        analyst.disburseLoan(loan, loanAccount);
        System.out.println("Préstamo desembolsado: " + loan);
        System.out.println("Saldo cuenta destino: " + loanAccount.getCurrentBalance());

        // ── Crear una transferencia ────────────────────────────────
        Transfer transfer = new Transfer(
                "TRF-001",
                account.getAccountNumber(),
                loanAccount.getAccountNumber(),
                5000.00,
                client.getUserId()
        );
        System.out.println("\nTransferencia creada: " + transfer);

        transfer.execute(account, loanAccount);
        System.out.println("Transferencia ejecutada: " + transfer);
        System.out.println("Saldo cuenta origen: " + account.getCurrentBalance());
        System.out.println("Saldo cuenta destino: " + loanAccount.getCurrentBalance());

        // ──  Registrar en la bitacora ───────────────────────────────
        AuditLog log = new AuditLog(
                "LOG-001",
                AuditOperationType.TRANSFER_EXECUTED,
                client.getUserId(),
                client.getRole().toString(),
                transfer.getTransferId()
        );
        TransferDetail detail = new TransferDetail(
                5000.00,
                50000.00,
                45000.00,
                18000.00,
                23000.00
        );
        log.setTransferDetail(detail);
        System.out.println("\nBitácora registrada: " + log);

    }
}