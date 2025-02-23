package com.microservice_users.microservice_users.Service;

import com.microservice_users.microservice_users.Client.BookFeignClient;
import com.microservice_users.microservice_users.Entities.Loan;
import com.microservice_users.microservice_users.Entities.LoanBook;
import com.microservice_users.microservice_users.Entities.User;
import com.microservice_users.microservice_users.Repository.ILoanRepository;
import com.microservice_users.microservice_users.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private ILoanRepository loanRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private BookFeignClient bookFeignClient;

    public Loan createLoan(int userId, Long bookId, Date dateOfDevolution) {
        // Validar la existencia del usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Crear el préstamo
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setAcquisition_date(new Date());
        loan.setConfirm_devolution(false);
        loan.setDate_of_devolution(dateOfDevolution);

        // Asociar el libro al préstamo
        var bookResponse = bookFeignClient.getBook(bookId);
        if (bookResponse.getBody() == null) {
            throw new RuntimeException("Book with ID " + bookId + " not found");
        }

        LoanBook loanBook = new LoanBook();
        loanBook.setBookId(bookId);
        loanBook.setLoan(loan);
        loan.getLoanBooks().add(loanBook);

        return loanRepository.save(loan);
    }


    // Get loan by ID
    public Loan getLoanById(int loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    // Get all loans
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    // Confirm devolution of a loan
    public Loan confirmDevolution(int loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setConfirm_devolution(true);
        loan.setDate_of_devolution(new Date());

        return loanRepository.save(loan);
    }

    // Delete loan
    public void deleteLoan(int loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loanRepository.delete(loan);
    }
}
