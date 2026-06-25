package com.noc.employee_cv.authentication;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @NotBlank(message = "username is required")
    @Size(min = 3, max = 50, message = "username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "firstname is required")
    @Size(max = 100, message = "firstname must be 100 characters or less")
    private String firstname;

    @NotBlank(message = "password is required")
    @Size(min = 8, max = 72, message = "password must be between 8 and 72 characters")
    private String password;

    @NotBlank(message = "lastname is required")
    @Size(max = 100, message = "lastname must be 100 characters or less")
    private String lastname;

    @Email(message = "incorrect email format")
    @Size(max = 254, message = "email must be 254 characters or less")
    private String email;
//    CHANGE TO LIST LATER ON
    @NotBlank(message = "role is required")
    private String role;
}
