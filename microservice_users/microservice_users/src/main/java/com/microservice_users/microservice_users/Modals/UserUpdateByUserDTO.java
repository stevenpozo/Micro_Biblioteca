package com.microservice_users.microservice_users.Modals;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateByUserDTO {

    @NotBlank(message = "First name cannot be blank")
    private String first_name;

    @NotBlank(message = "Last name cannot be blank")
    private String last_name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String mail;
}
