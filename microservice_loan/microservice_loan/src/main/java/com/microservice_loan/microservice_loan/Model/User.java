package com.microservice_loan.microservice_loan.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_user;

    private String first_name;

    private String last_name;

    private String mail;

    private String role;

    private String password;

    private boolean status;

    private String code;

    private int failed_attempts;

    private Timestamp lock_time;
}
