package app.application.adapter.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import app.application.usecases.CreateBankAccountUseCase;
import app.domain.models.BankAccount;
import app.domain.services.commands.CreateBankAccountCommand;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final CreateBankAccountUseCase createBankAccountUseCase;

    public AccountController(CreateBankAccountUseCase createBankAccountUseCase) {
        this.createBankAccountUseCase = createBankAccountUseCase;
    }

    @GetMapping
    public ResponseEntity<List<BankAccount>> listAccounts(Authentication auth) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PostMapping
    public ResponseEntity<BankAccount> createAccount(
            @RequestBody CreateBankAccountCommand command,
            Authentication auth) {
        BankAccount account = createBankAccountUseCase.execute(command);
        return ResponseEntity.ok(account);
    }
}
