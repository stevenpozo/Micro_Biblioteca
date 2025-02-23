package com.microservice_books.microservice_books.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

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
    


    //GETTERS AND SETTERS


    public int getId_book() {
        return id_book;
    }

    public void setId_book(int id_book) {
        this.id_book = id_book;
    }

    public @NotBlank(message = "Title cannot be empty") @Size(max = 100, message = "Title must be at most 100 characters") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title cannot be empty") @Size(max = 100, message = "Title must be at most 100 characters") String title) {
        this.title = title;
    }

    public @NotBlank(message = "Author cannot be empty") @Size(max = 50, message = "Author must be at most 50 characters") @Pattern(regexp = "^[A-Za-zÑñÁáÉéÍíÓóÚú ]+$", message = "Author must contain only letters") String getAuthor() {
        return author;
    }

    public void setAuthor(@NotBlank(message = "Author cannot be empty") @Size(max = 50, message = "Author must be at most 50 characters") @Pattern(regexp = "^[A-Za-zÑñÁáÉéÍíÓóÚú ]+$", message = "Author must contain only letters") String author) {
        this.author = author;
    }

    public @NotBlank(message = "Language cannot be empty") @Size(max = 30, message = "Language must be at most 30 characters") @Pattern(regexp = "^[A-Za-zÑñÁáÉéÍíÓóÚú ]+$", message = "Language must contain only letters") String getLanguage() {
        return language;
    }

    public void setLanguage(@NotBlank(message = "Language cannot be empty") @Size(max = 30, message = "Language must be at most 30 characters") @Pattern(regexp = "^[A-Za-zÑñÁáÉéÍíÓóÚú ]+$", message = "Language must contain only letters") String language) {
        this.language = language;
    }

    public @NotBlank(message = "Code cannot be empty") @Size(max = 20, message = "Code must be at most 20 characters") @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "Code must be alphanumeric with hyphens allowed") String getCode() {
        return code;
    }

    public void setCode(@NotBlank(message = "Code cannot be empty") @Size(max = 20, message = "Code must be at most 20 characters") @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "Code must be alphanumeric with hyphens allowed") String code) {
        this.code = code;
    }

    public @Size(max = 255, message = "Description must be at most 255 characters") String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 255, message = "Description must be at most 255 characters") String description) {
        this.description = description;
    }

    public @NotBlank(message = "Physical state cannot be empty") @Size(max = 50, message = "Physical state must be at most 50 characters") String getPhysical_state() {
        return physical_state;
    }

    public void setPhysical_state(@NotBlank(message = "Physical state cannot be empty") @Size(max = 50, message = "Physical state must be at most 50 characters") String physical_state) {
        this.physical_state = physical_state;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public @NotNull(message = "Price cannot be null") @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0") @Digits(integer = 10, fraction = 2, message = "Price must be a valid monetary amount") BigDecimal getPrice() {
        return price;
    }

    public void setPrice(@NotNull(message = "Price cannot be null") @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0") @Digits(integer = 10, fraction = 2, message = "Price must be a valid monetary amount") BigDecimal price) {
        this.price = price;
    }
}
