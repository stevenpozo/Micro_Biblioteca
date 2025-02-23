package com.microservice_users.microservice_users.Service;

import com.microservice_users.microservice_users.Client.BookFeignClient;
import com.microservice_users.microservice_users.Entities.Loan;
import com.microservice_users.microservice_users.Entities.LoanBook;
import com.microservice_users.microservice_users.Entities.User;
import com.microservice_users.microservice_users.Models.Book;
import com.microservice_users.microservice_users.Models.LoanUserBookDTO;
import com.microservice_users.microservice_users.Repository.ILoanRepository;
import com.microservice_users.microservice_users.Repository.IUserRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    //CREATE A NEW LOAN
    public Loan createLoan(int userId, Long bookId, Date dateOfDevolution) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.isStatus()){
            throw new RuntimeException("This user is disable");
        }

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setAcquisition_date(new Date());
        loan.setConfirm_devolution(false);
        loan.setDate_of_devolution(dateOfDevolution);

        try {
            var bookResponse = bookFeignClient.getBook(bookId);
            if (bookResponse == null || bookResponse.getBody() == null) {
                throw new RuntimeException("No found this book");
            }

            ResponseEntity<Boolean> bookStatusResponse = bookFeignClient.verifyBookStatus(bookId.intValue());
            if (bookStatusResponse == null || !bookStatusResponse.getBody()) {
                throw new RuntimeException("This book is disable");
            }

            bookFeignClient.disableBook(bookId.intValue());

        } catch (FeignException.NotFound e) {
            throw new RuntimeException("No found this book");
        } catch (FeignException e) {
            throw new RuntimeException("Error server");
        }

        LoanBook loanBook = new LoanBook();
        loanBook.setBookId(bookId);
        loanBook.setLoan(loan);
        loan.getLoanBooks().add(loanBook);

        return loanRepository.save(loan);
    }




    //GET LOAN BY ID
    public LoanUserBookDTO getLoanById(int loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        LoanUserBookDTO loanUserBookDTO = new LoanUserBookDTO();
        loanUserBookDTO.setId_loan(loan.getId());
        loanUserBookDTO.setCodeUser(loan.getUser().getCode());
        loanUserBookDTO.setUser_name(loan.getUser().getFirst_name());
        loanUserBookDTO.setUser_last_name(loan.getUser().getLast_name());
        loanUserBookDTO.setAcquisition_date(loan.getAcquisition_date());
        loanUserBookDTO.setDate_of_devolution(loan.getDate_of_devolution());

        for (LoanBook loanBook : loan.getLoanBooks()) {
            Book book = bookFeignClient.getBook(loanBook.getBookId()).getBody();

            loanUserBookDTO.setCodeBook(book.getCode());
            loanUserBookDTO.setTitle(book.getTitle());
            loanUserBookDTO.setAuthor(book.getAuthor());
        }

        return loanUserBookDTO;
    }

    //GET ALL LOAN
    public List<LoanUserBookDTO> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        List<LoanUserBookDTO> loanDTOs = new ArrayList<>();

        for (Loan loan : loans) {
            LoanUserBookDTO loanDTO = new LoanUserBookDTO();

            loanDTO.setId_loan(loan.getId());
            loanDTO.setCodeUser(loan.getUser().getCode());
            loanDTO.setUser_name(loan.getUser().getFirst_name());
            loanDTO.setUser_last_name(loan.getUser().getLast_name());
            loanDTO.setAcquisition_date(loan.getAcquisition_date());
            loanDTO.setDate_of_devolution(loan.getDate_of_devolution());

            for (LoanBook loanBook : loan.getLoanBooks()) {
                Book book = bookFeignClient.getBook(loanBook.getBookId()).getBody();

                if (book != null) {
                    loanDTO.setCodeBook(book.getCode());
                    loanDTO.setTitle(book.getTitle());
                    loanDTO.setAuthor(book.getAuthor());
                }
            }

            loanDTOs.add(loanDTO);
        }

        return loanDTOs;
    }


    //CONFIRM DEVOLUTION LOAN
    public Loan confirmDevolution(int loanId, Long bookId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        var bookResponse = bookFeignClient.getBook(bookId);
        if (bookResponse == null || bookResponse.getBody() == null) {
            throw new RuntimeException("No found this book");
        }

        loan.setConfirm_devolution(true);
        loan.setDate_of_devolution(new Date());

        bookFeignClient.enableBook(bookId.intValue());

        return loanRepository.save(loan);
    }


    // DISABLE LOAN
    public void disableLoan(int loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setConfirm_devolution(true);
        loanRepository.save(loan);
    }
}
