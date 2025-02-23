package com.microservice_users.microservice_users.Entities;

import java.util.Date;
import java.util.List;

public class LoanRequest {
    private int userId;
    private Long bookId;  // Ahora solo recibes un libro
    private Date dateOfDevolution; // Fecha de devolución enviada en el cuerpo

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Date getDateOfDevolution() {
        return dateOfDevolution;
    }

    public void setDateOfDevolution(Date dateOfDevolution) {
        this.dateOfDevolution = dateOfDevolution;
    }
}
