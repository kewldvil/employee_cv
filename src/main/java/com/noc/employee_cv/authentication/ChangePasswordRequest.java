package com.noc.employee_cv.authentication;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChangePasswordRequest {
    private Integer userId;
    private String oldPassword;
    private String newPassword;
}
