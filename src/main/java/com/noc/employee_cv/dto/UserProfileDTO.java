package com.noc.employee_cv.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {
    private Integer id;
    private String lastname;
    private String firstname;
    private String email;
    private String role;
    private String username;
}
