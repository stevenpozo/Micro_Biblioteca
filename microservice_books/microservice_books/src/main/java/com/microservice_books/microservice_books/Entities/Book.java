package com.microservice_books.microservice_books.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Table(name = "book")
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_book")
    private int id_book;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 100, message = "Title must be at most 100 characters")
    @Column(name = "title")
    private String title;

    @NotBlank(message = "Author cannot be empty")
    @Size(max = 50, message = "Author must be at most 50 characters")
    @Pattern(regexp = "^[A-Za-zÑñÁáÉéÍíÓóÚú ]+$", message = "Author must contain only letters")
    @Column(name = "author")
    private String author;

    @NotBlank(message = "Language cannot be empty")
    @Size(max = 30, message = "Language must be at most 30 characters")
    @Pattern(regexp = "^[A-Za-zÑñÁáÉéÍíÓóÚú ]+$", message = "Language must contain only letters")
    @Column(name = "language")
    private String language;

    @NotBlank(message = "Code cannot be empty")
    @Size(max = 20, message = "Code must be at most 20 characters")
    @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "Code must be alphanumeric with hyphens allowed")
    @Column(name = "code")
    private String code;

    @Size(max = 255, message = "Description must be at most 255 characters")
    @Column(name = "description")
    private String description;

    @NotBlank(message = "Physical state cannot be empty")
    @Size(max = 50, message = "Physical state must be at most 50 characters")
    @Column(name = "physical_state")
    private String physical_state;

    @Column(name = "status", columnDefinition = "TINYINT(1)")
    private Boolean status;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must be a valid monetary amount")
    @Column(name = "price")
    private BigDecimal price;

}
