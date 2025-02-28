package com.microservice_loan.microservice_loan.Repository;

import com.microservice_loan.microservice_loan.Entities.LoanBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILoanBookRepository extends JpaRepository<LoanBook, Long> {

}
