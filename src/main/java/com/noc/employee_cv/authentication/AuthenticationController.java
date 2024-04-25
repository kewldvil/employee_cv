package com.noc.employee_cv.authentication;

import com.noc.employee_cv.models.Role;
import com.noc.employee_cv.models.User;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth/authenticate")
@RequiredArgsConstructor
//@Tag(name="Authentication")
public class AuthenticationController {
    private final AuthenticationService service;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(

            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {

        service.register(request);
        return ResponseEntity.accepted().build();
    }

    //    user login
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);
    }
}
