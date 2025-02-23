package com.microservice_loan.microservice_loan.Repository;

import com.microservice_loan.microservice_loan.Entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILoanRepository extends JpaRepository<Loan, Long> {
}

