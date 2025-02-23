package com.microservice_users.microservice_users.Repository;

import com.microservice_users.microservice_users.Entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ILoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findById(int id);
}
