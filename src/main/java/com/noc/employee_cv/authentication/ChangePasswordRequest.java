package com.noc.employee_cv.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotNull(message = "userId is required")
    private Integer userId;

    @NotBlank(message = "oldPassword is required")
    private String oldPassword;

    @NotBlank(message = "newPassword is required")
    @Size(min = 8, max = 72, message = "newPassword must be between 8 and 72 characters")
    private String newPassword;
}
