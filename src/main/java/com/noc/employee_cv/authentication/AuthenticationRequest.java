package com.noc.employee_cv.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {
    @NotEmpty(message = "មិនអាចទទេរ!")
    @NotBlank(message = "មិនអាចទទេរ!")
    @Size(max = 50, message = "username must be 50 characters or less")
    private String username;
    @NotEmpty(message = "មិនអាចទទេរ!")
    @NotBlank(message = "មិនអាចទទេរ!")
    @Size(min = 8, max = 72, message = "password must be between 8 and 72 characters")
    private String password;



}
