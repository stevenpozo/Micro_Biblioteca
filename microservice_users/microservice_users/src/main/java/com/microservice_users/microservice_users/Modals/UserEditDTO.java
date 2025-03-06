package com.microservice_users.microservice_users.Modals;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEditDTO {

    @NotBlank
    private String first_name;

    @NotBlank
    private String last_name;

    @NotBlank
    @Email
    private String mail;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private String role;

    private String code;
}
