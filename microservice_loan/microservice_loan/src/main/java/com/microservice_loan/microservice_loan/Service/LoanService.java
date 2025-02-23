package com.microservice_loan.microservice_loan.Service;

import com.microservice_loan.microservice_loan.Client.ClientBook;
import com.microservice_loan.microservice_loan.Client.ClientUser;
import com.microservice_loan.microservice_loan.Entities.Loan;
import com.microservice_loan.microservice_loan.Entities.LoanBookUser;
import com.microservice_loan.microservice_loan.Model.Book;
import com.microservice_loan.microservice_loan.Model.User;
import com.microservice_loan.microservice_loan.Repository.ILoanRepository;
import com.microservice_loan.microservice_loan.Repository.ILoanUserBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private ILoanRepository loanRepository;

    @Autowired
    private ILoanUserBookRepository loanUserBookRepository;

    @Autowired
    private ClientUser clientUser;

    @Autowired
    private ClientBook clientBook;

    // GET ALL LOANS
    public List<Loan> getAllLoan() {
        try {
            List<Loan> loans = loanRepository.findAll();
            return loans.isEmpty() ? new ArrayList<>() : loans;
        } catch (Exception e) {
            throw new RuntimeException("Error getting loan list", e);
        }
    }

    // CREATE LOAN
    public Loan createLoan(Long userId, Long bookId, Loan loans) {
        try {
            // Verificar si el usuario y el libro existen
            User userDTO = clientUser.getUserById(userId);
            Book bookDTO = clientBook.getBookById(bookId);

            if (userDTO == null || bookDTO == null) {
                throw new RuntimeException("User or Book not found");
            }

            // Crear y guardar el préstamo
            Loan loan = new Loan();

            // Asignar la fecha actual como fecha de adquisición
            loan.setAcquisition_date(new Date());  // Establece la fecha actual

            // Utilizar la fecha de devolución proporcionada
            loan.setDate_of_devolution(loans.getDate_of_devolution());

            // Establecer confirmDevolution en true por defecto
            loan.setConfirm_devolution(true);

            loanRepository.save(loan);

            // Crear LoanBookUser con IDs en lugar de objetos completos
            LoanBookUser loanBookUser = new LoanBookUser();
            loanBookUser.setLoan(loan);
            loanBookUser.setUserId(userDTO.getId_user());  // Guardar solo el ID del usuario
            loanBookUser.setBookId(bookDTO.getId_book());  // Guardar solo el ID del libro

            loanUserBookRepository.save(loanBookUser);

            return loan;
        } catch (Exception e) {
            throw new RuntimeException("Error creating loan", e);
        }
    }

}
