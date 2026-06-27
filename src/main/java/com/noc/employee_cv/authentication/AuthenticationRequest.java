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
    @Size(max = 72, message = "password must be 72 characters or less")
    private String password;



}
