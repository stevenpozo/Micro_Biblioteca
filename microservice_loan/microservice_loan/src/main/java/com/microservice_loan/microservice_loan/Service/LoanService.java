package com.microservice_loan.microservice_loan.Service;

import com.microservice_loan.microservice_loan.Entities.Loan;
import com.microservice_loan.microservice_loan.Entities.LoanBook;
import com.microservice_loan.microservice_loan.Models.BinnacleDTO;
import com.microservice_loan.microservice_loan.Models.BookDTO;
import com.microservice_loan.microservice_loan.Models.LoanUserBookDTO;
import com.microservice_loan.microservice_loan.Models.UserDTO;
import com.microservice_loan.microservice_loan.Repository.ILoanRepository;
import com.microservice_loan.microservice_loan.client.BookFeignClient;
import com.microservice_loan.microservice_loan.client.UserFeignClient;
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
    private UserFeignClient userFeignClient;

    @Autowired
    private BookFeignClient bookFeignClient;


    // CREATE A NEW LOAN
    public Loan createLoan(int userId, Long bookId, Date dateOfDevolution) {
        // Consultar el microservicio de user para obtener la información del usuario
        ResponseEntity<UserDTO> userResponse = userFeignClient.getUserById((long) userId);
        UserDTO userDTO = userResponse.getBody();
        if (userDTO == null) {
            throw new RuntimeException("User not found");
        }
        if (!userDTO.isStatus()) {
            throw new RuntimeException("This user is disabled");
        }

        Loan loan = new Loan();
        // Almacenar sólo el ID del usuario
        loan.setUserId((long) userDTO.getId_user());
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
                throw new RuntimeException("This book is disabled");
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

    // GET LOAN BY ID
    public LoanUserBookDTO getLoanById(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        LoanUserBookDTO loanUserBookDTO = new LoanUserBookDTO();
        loanUserBookDTO.setId_loan(loan.getId());

        // Obtener datos de usuario desde el microservicio de user
        ResponseEntity<UserDTO> userResponse = userFeignClient.getUserById(loan.getUserId());
        UserDTO userDTO = userResponse.getBody();
        if (userDTO != null) {
            loanUserBookDTO.setCodeUser(userDTO.getCode());
            loanUserBookDTO.setUser_name(userDTO.getFirst_name());
            loanUserBookDTO.setUser_last_name(userDTO.getLast_name());
        }
        loanUserBookDTO.setAcquisition_date(loan.getAcquisition_date());
        loanUserBookDTO.setDate_of_devolution(loan.getDate_of_devolution());

        for (LoanBook loanBook : loan.getLoanBooks()) {
            BookDTO book = bookFeignClient.getBook(loanBook.getBookId()).getBody();
            if (book != null) {
                loanUserBookDTO.setCodeBook(book.getCode());
                loanUserBookDTO.setTitle(book.getTitle());
            }
        }

        return loanUserBookDTO;
    }

    // GET SOME DATA LOAN
    public List<LoanUserBookDTO> getAllLoanUserBook() {
        List<Loan> loans = loanRepository.findAll();
        List<LoanUserBookDTO> loanDTOs = new ArrayList<>();

        for (Loan loan : loans) {
            LoanUserBookDTO loanDTO = new LoanUserBookDTO();
            loanDTO.setId_loan(loan.getId());

            // Obtener datos de usuario
            ResponseEntity<UserDTO> userResponse = userFeignClient.getUserById(loan.getUserId());
            UserDTO userDTO = userResponse.getBody();
            if (userDTO != null) {
                loanDTO.setCodeUser(userDTO.getCode());
                loanDTO.setUser_name(userDTO.getFirst_name());
                loanDTO.setUser_last_name(userDTO.getLast_name());
            }
            loanDTO.setAcquisition_date(loan.getAcquisition_date());
            loanDTO.setDate_of_devolution(loan.getDate_of_devolution());

            for (LoanBook loanBook : loan.getLoanBooks()) {
                BookDTO book = bookFeignClient.getBook(loanBook.getBookId()).getBody();
                if (book != null) {
                    loanDTO.setCodeBook(book.getCode());
                    loanDTO.setTitle(book.getTitle());
                }
            }

            loanDTOs.add(loanDTO);
        }

        return loanDTOs;
    }

    // GET BINNACLE
    public List<BinnacleDTO> getAllBinnacleData() {
        List<Loan> loans = loanRepository.findAll();
        List<BinnacleDTO> binnacleDTOs = new ArrayList<>();

        for (Loan loan : loans) {
            BinnacleDTO dto = new BinnacleDTO();

            // Obtener información del usuario desde el microservicio de user
            ResponseEntity<UserDTO> userResponse = userFeignClient.getUserById(loan.getUserId());
            UserDTO userDTO = userResponse.getBody();
            if (userDTO != null) {
                dto.setUser_name(userDTO.getFirst_name());
                dto.setUser_last_name(userDTO.getLast_name());
                dto.setMail(userDTO.getMail());
                dto.setRole(userDTO.getRole());
            }

            dto.setAcquisition_date(loan.getAcquisition_date());
            dto.setDate_of_devolution(loan.getDate_of_devolution());
            dto.setConfirm_devolution(loan.isConfirm_devolution());

            if (loan.getLoanBooks() != null) {
                for (LoanBook loanBook : loan.getLoanBooks()) {
                    try {
                        BookDTO book = bookFeignClient.getBook(loanBook.getBookId()).getBody();
                        if (book != null) {
                            dto.setCodeBook(book.getCode());
                            dto.setTitle(book.getTitle());
                            dto.setAuthor(book.getAuthor());
                            dto.setLanguage(book.getLanguage());
                        }
                    } catch (Exception e) {
                        System.out.println("Error get book by id: " + loanBook.getBookId());
                    }
                }
            }

            binnacleDTOs.add(dto);
        }

        return binnacleDTOs;
    }

    // CONFIRM DEVOLUTION LOAN
    public Loan returnedBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getLoanBooks() == null || loan.getLoanBooks().isEmpty()) {
            throw new RuntimeException("No associated books found for this loan");
        }

        Long bookId = loan.getLoanBooks().get(0).getBookId();

        var bookResponse = bookFeignClient.getBook(bookId);
        if (bookResponse == null || bookResponse.getBody() == null) {
            throw new RuntimeException("Book not found");
        }

        loan.setConfirm_devolution(true);
        loan.setDate_of_devolution(new Date());

        bookFeignClient.enableBook(bookId.intValue());

        return loanRepository.save(loan);
    }


    // DISABLE LOAN
    public void disableLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setConfirm_devolution(true);
        loanRepository.save(loan);
    }

    //Update loan
    public Loan updateLoan(Loan request, Long id){
        Loan loan = loanRepository.findById(id).get();
        loan.setDate_of_devolution(request.getDate_of_devolution());
        return loanRepository.save(loan);
    }


    //GET LOANS
    public List<Loan> getAllLoans(){
        return loanRepository.findAll();
    }


}
