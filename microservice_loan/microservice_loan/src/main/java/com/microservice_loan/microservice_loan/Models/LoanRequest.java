package com.microservice_loan.microservice_loan.Models;

import java.util.Date;

public class LoanRequest {
    private int userId;
    private Long bookId;
    private Date dateOfDevolution;

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
