package com.microservice_loan.microservice_loan.Controller;

import com.microservice_loan.microservice_loan.DTO.LoanRequestDTO;
import com.microservice_loan.microservice_loan.Entities.Loan;
import com.microservice_loan.microservice_loan.Service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    // Obtener todos los préstamos
    @GetMapping
    public ResponseEntity<?> getAllLoans() {
        try {
            return new ResponseEntity<>(loanService.getAllLoan(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody LoanRequestDTO loanRequest) {
        try {
            Loan loan = new Loan();

            // Enviar solo la fecha de devolución y los IDs del usuario y libro
            loan.setDate_of_devolution(loanRequest.getDateOfDevolution());

            // Llamar a tu servicio para crear el préstamo, pasando los IDs y el objeto Loan
            Loan savedLoan = loanService.createLoan(
                    loanRequest.getUserId(),
                    loanRequest.getBookId(),
                    loan
            );

            return new ResponseEntity<>(savedLoan, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
