package com.noc.employee_cv.authentication;


import com.noc.employee_cv.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String token;
    private User user;
    @JsonIgnore
    private String refreshToken;
}
