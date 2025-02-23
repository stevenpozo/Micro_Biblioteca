package com.microservice_users.microservice_users.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Table(name = "user")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_user")
    private int id_user;

    @NotBlank(message = "First name empty")
    @Size(max = 50, message = "First name must be at most 50 characters")
    @Pattern(
            regexp = "^[A-Za-zÑñÁáÉéÍíÓóÚú]{1,15}( [A-Za-zÑñÁáÉéÍíÓóÚú]{1,15})*( [A-Za-zÑñÁáÉéÍíÓóÚú]{1,15})?$",
            message = "First name must contain only letters"
    )
    @Column(name = "first_name")
    private String first_name;


    @NotBlank(message = "Last name empty")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    @Pattern(
            regexp = "^[A-Za-zÑñÁáÉéÍíÓóÚú]{1,15}( [A-Za-zÑñÁáÉéÍíÓóÚú]{1,15})*( [A-Za-zÑñÁáÉéÍíÓóÚú]{1,15})?$",
            message = "Last name must contain only letters"
    )
    @Column(name = "last_name")
    private String last_name;


    @NotBlank(message = "Email empty")
    @Email(message = "Email should be valid")
    @Column(name = "mail")
    private String mail;

    private String role;


    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(name = "password")
    private String password;

    @Column(name = "status", columnDefinition = "TINYINT(1)")
    private boolean status;

    @Column(name= "code")
    private String code;

    @Column(name = "failed_attempts")
    private int failed_attempts;

    @Column(name="lock_time")
    private Timestamp lock_time;
}
