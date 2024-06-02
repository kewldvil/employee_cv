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
    private String username;
    @NotEmpty(message = "មិនអាចទទេរ!")
    @NotBlank(message = "មិនអាចទទេរ!")
    @Size(min = 8, message = "យ៉ាងតិច៨តួរ")
    private String password;



}
