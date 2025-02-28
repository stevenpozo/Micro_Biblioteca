package com.microservice_loan.microservice_loan.Controller;

import com.microservice_loan.microservice_loan.Entities.Loan;
import com.microservice_loan.microservice_loan.Models.BinnacleDTO;
import com.microservice_loan.microservice_loan.Models.LoanRequest;
import com.microservice_loan.microservice_loan.Models.LoanUserBookDTO;
import com.microservice_loan.microservice_loan.Service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/loan")
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    private LoanService loanService;

    // CREATE A NEW LOAN
    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody LoanRequest loanRequest) {
        try {
            Loan newLoan = loanService.createLoan(
                    loanRequest.getUserId(),
                    loanRequest.getBookId(),
                    loanRequest.getDateOfDevolution()
            );
            return new ResponseEntity<>(newLoan, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // GET LOAN BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getLoanById(@PathVariable Long id) {
        try {
            LoanUserBookDTO loan = loanService.getLoanById(id);

            if (loan == null) {
                return new ResponseEntity<>("Loan not found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(loan, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // GET SOME DATA LOAN
    @GetMapping("/some-data")
    public ResponseEntity<List<LoanUserBookDTO>> getAllLoans() {
        try {
            List<LoanUserBookDTO> loans = loanService.getAllLoanUserBook();

            if (loans.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(loans, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET BINNACLE
    @GetMapping("/binnacle")
    public ResponseEntity<List<BinnacleDTO>> getAllBinacleEntries() {
        List<BinnacleDTO> entries = loanService.getAllBinnacleData();

        if (entries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(entries);
    }

    // CONFIRM DEVOLUTION
    @PutMapping("/confirm-devolution/{loanId}/{bookId}")
    public ResponseEntity<?> confirmDevolution(@PathVariable Long loanId, @PathVariable Long bookId) {
        try {
            Loan updatedLoan = loanService.confirmDevolution(loanId, bookId);
            return new ResponseEntity<>(updatedLoan, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // DISABLE LOAN
    @PutMapping("/disable-loan/{id}")
    public ResponseEntity<?> disableLoan(@PathVariable Long id) {
        try {
            loanService.disableLoan(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public Loan updateLoan(@RequestBody Loan loanModel,@PathVariable("id") Long id){
        return this.loanService.updateLoan(loanModel, id);
    }

}
