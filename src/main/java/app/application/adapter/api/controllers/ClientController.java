package app.application.adapter.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import app.application.usecases.NaturalClientUseCase;
import app.domain.models.NaturalPersonClient;
import app.domain.models.Loan;
import app.domain.models.BankAccount;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final NaturalClientUseCase naturalClientUseCase;

    public ClientController(NaturalClientUseCase naturalClientUseCase) {
        this.naturalClientUseCase = naturalClientUseCase;
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<NaturalPersonClient> getClientDetails(
            @PathVariable String clientId,
            Authentication auth) {
        String employeeId = auth.getName();
        NaturalPersonClient client = naturalClientUseCase.getClientDetails(employeeId, clientId);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/{clientId}/loans")
    public ResponseEntity<List<Loan>> listClientLoans(
            @PathVariable String clientId,
            Authentication auth) {
        String employeeId = auth.getName();
        List<Loan> loans = naturalClientUseCase.listClientLoans(employeeId, clientId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{clientId}/accounts")
    public ResponseEntity<List<BankAccount>> listClientAccounts(
            @PathVariable String clientId,
            Authentication auth) {
        String employeeId = auth.getName();
        List<BankAccount> accounts = naturalClientUseCase.listClientAccounts(employeeId, clientId);
        return ResponseEntity.ok(accounts);
    }
}
