package com.microservice_users.microservice_users.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @ManyToOne
    @JoinColumn(name = "user_id_user", referencedColumnName = "id_user")
    private User user;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Indicates the owner side of the relationship
    private List<LoanBook> loanBooks = new ArrayList<>();


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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<LoanBook> getLoanBooks() {
        return loanBooks;
    }

    public void setLoanBooks(List<LoanBook> loanBooks) {
        this.loanBooks = loanBooks;
    }
}
