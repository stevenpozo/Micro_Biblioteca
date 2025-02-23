package com.microservice_loan.microservice_loan.Repository;

import com.microservice_loan.microservice_loan.Entities.LoanBookUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ILoanUserBookRepository extends JpaRepository<LoanBookUser, Long> {
    List<LoanBookUser> findByLoan_Id(Long loanId);

    Optional<LoanBookUser> findByLoanIdAndBookIdAndUserId(Long loanId, Long bookId, Long userId);
}
