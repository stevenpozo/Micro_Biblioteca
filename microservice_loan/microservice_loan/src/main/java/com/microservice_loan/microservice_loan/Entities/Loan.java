package com.microservice_loan.microservice_loan.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "loan")
@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "acquisition_date")
    private Date acquisition_date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date_of_devolution")
    private Date date_of_devolution;

    @Column(name = "confirm_devolution")
    private boolean confirm_devolution;

    // Almacenar solo el ID del usuario, no la entidad completa
    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanBook> loanBooks = new ArrayList<>();

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getAcquisition_date() {
        return acquisition_date;
    }

    public void setAcquisition_date(Date acquisition_date) {
        this.acquisition_date = acquisition_date;
    }

    public Date getDate_of_devolution() {
        return date_of_devolution;
    }

    public void setDate_of_devolution(Date date_of_devolution) {
        this.date_of_devolution = date_of_devolution;
    }

    public boolean isConfirm_devolution() {
        return confirm_devolution;
    }

    public void setConfirm_devolution(boolean confirm_devolution) {
        this.confirm_devolution = confirm_devolution;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<LoanBook> getLoanBooks() {
        return loanBooks;
    }

    public void setLoanBooks(List<LoanBook> loanBooks) {
        this.loanBooks = loanBooks;
    }
}
