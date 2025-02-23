package com.microservice_loan.microservice_loan.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_book;

    private String title;

    private String author;

    private String language;

    private String code;

    private String description;

    private String physical_state;

    private Boolean status;

    private BigDecimal price;
}
