package app.application.adapter.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import app.application.usecases.ApproveLoanUseCase;
import app.application.usecases.RejectLoanUseCase;
import app.application.usecases.DisburseLoanUseCase;
import app.domain.services.commands.ApproveLoanCommand;
import app.domain.services.commands.RejectLoanCommand;
import app.domain.services.commands.DisburseLoanCommand;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final ApproveLoanUseCase approveLoanUseCase;
    private final RejectLoanUseCase rejectLoanUseCase;
    private final DisburseLoanUseCase disburseLoanUseCase;

    public LoanController(ApproveLoanUseCase approveLoanUseCase,
                         RejectLoanUseCase rejectLoanUseCase,
                         DisburseLoanUseCase disburseLoanUseCase) {
        this.approveLoanUseCase = approveLoanUseCase;
        this.rejectLoanUseCase = rejectLoanUseCase;
        this.disburseLoanUseCase = disburseLoanUseCase;
    }

    @PostMapping("/{loanId}/approve")
    public ResponseEntity<Void> approveLoan(
            @PathVariable String loanId,
            @RequestBody ApproveLoanCommand command,
            Authentication auth) {
        command.setLoanId(loanId);
        approveLoanUseCase.execute(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{loanId}/reject")
    public ResponseEntity<Void> rejectLoan(
            @PathVariable String loanId,
            @RequestBody RejectLoanCommand command,
            Authentication auth) {
        command.setLoanId(loanId);
        rejectLoanUseCase.execute(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{loanId}/disburse")
    public ResponseEntity<Void> disburseLoan(
            @PathVariable String loanId,
            @RequestBody DisburseLoanCommand command,
            Authentication auth) {
        command.setLoanId(loanId);
        disburseLoanUseCase.execute(command);
        return ResponseEntity.ok().build();
    }
}
