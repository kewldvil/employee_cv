package com.noc.employee_cv.authentication;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
public class ForgetPasswordRequest {
    @NotBlank(message = "phoneNumber is required")
    @Size(max = 30, message = "phoneNumber must be 30 characters or less")
    private String phoneNumber;
    @NotBlank(message = "មិនអាចទទេរ!")
    @Size(min = 8, max = 72, message = "password must be between 8 and 72 characters")
    private String password;
    @NotBlank(message = "មិនអាចទទេរ!")
    @Size(max = 50, message = "username must be 50 characters or less")
    private String username;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull(message = "dateOfBirth is required")
    private LocalDate dateOfBirth;
}
