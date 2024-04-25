package com.noc.employee_cv.authentication;

import com.noc.employee_cv.models.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@ToString
public class RegistrationRequest {
    @NotEmpty(message = "not blank")
    @NotBlank(message = "not blank")
    @NotNull(message = "not blank")
    private String username;
    @NotEmpty(message = "not blank")
    @NotBlank(message = "not blank")
    @NotNull(message = "not blank")
    private String firstname;
    @NotEmpty(message = "not blank")
    @NotBlank(message = "not blank")
    @NotNull(message = "not blank")
    @Size(min = 8, message = "យ៉ាងតិច៨តួរ")
    private String password;
    @NotEmpty(message = "not blank")
    @NotBlank(message = "not blank")
    @NotNull(message = "not blank")
    private String lastname;
    @Email(message = "incorrect email format")
    private String email;
//    CHANGE TO LIST LATER ON
    @NotEmpty(message = "not blank")
    @NotBlank(message = "not blank")
    @NotNull(message = "not blank")
    private String roles;
}
