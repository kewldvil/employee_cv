package com.noc.employee_cv.authentication;


import com.noc.employee_cv.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String token;
    private User user;
}
