package com.microservice_loan.microservice_loan.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class LoanBookUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    @JsonBackReference // Evita la recursión infinita durante la serialización
    private Loan loan;

    // Guarda solo el ID del libro en lugar de un objeto Book completo
    @Column(name = "book_id", nullable = false)
    private int bookId;

    // Guarda solo el ID del usuario en lugar de un objeto User completo
    @Column(name = "user_id", nullable = false)
    private int userId;

    private LocalDate enrollmentDate = LocalDate.now();
}
