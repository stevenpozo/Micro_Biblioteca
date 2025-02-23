package com.microservice_users.microservice_users.Controller;

import com.microservice_users.microservice_users.Entities.Loan;
import com.microservice_users.microservice_users.Entities.LoanRequest;
import com.microservice_users.microservice_users.Service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loan")
@CrossOrigin(origins = "http://localhost:5173")
public class LoanController {

    @Autowired
    private LoanService loanService;

    // Create a new loan
    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody LoanRequest loanRequest) {
        try {
            // Crear un nuevo préstamo con la fecha de adquisición como la fecha actual
            Loan newLoan = loanService.createLoan(
                    loanRequest.getUserId(),
                    loanRequest.getBookId(),
                    loanRequest.getDateOfDevolution() // Usamos la fecha de devolución proporcionada
            );
            return new ResponseEntity<>(newLoan, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // Get a loan by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getLoanById(@PathVariable int id) {
        try {
            Loan loan = loanService.getLoanById(id);
            return new ResponseEntity<>(loan, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Get all loans
    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }

    // Confirm devolution of a loan
    @PutMapping("confirm-devolution/{id}")
    public ResponseEntity<?> confirmDevolution(@PathVariable int id) {
        try {
            Loan updatedLoan = loanService.confirmDevolution(id);
            return new ResponseEntity<>(updatedLoan, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Delete a loan
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLoan(@PathVariable int id) {
        try {
            loanService.deleteLoan(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
