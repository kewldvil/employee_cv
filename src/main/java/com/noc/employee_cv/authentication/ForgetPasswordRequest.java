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
@ToString
public class ForgetPasswordRequest {
    @NotBlank
    private String phoneNumber;
    @NotBlank(message = "មិនអាចទទេរ!")
    @Size(min = 8, message = "យ៉ាងតិច៨តួរ")
    private String password;
    @NotBlank(message = "មិនអាចទទេរ!")
    private String username;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
}
