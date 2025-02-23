package com.microservice_users.microservice_users.Entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

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
