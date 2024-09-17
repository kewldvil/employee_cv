package com.noc.employee_cv.authentication;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/authenticate")
@RequiredArgsConstructor
//@Tag(name="Authentication")
public class AuthenticationController {
    private final AuthenticationService service;
    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        try {
            service.register(request);
            return ResponseEntity.accepted().build();
        } catch (IllegalArgumentException e) {
            // Return a conflict response with the error message if a username is taken
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    //    user login
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(
            @RequestBody @Valid ForgetPasswordRequest request
    ) throws MessagingException {
        try {
            service.forgetPassword(request);
            return ResponseEntity.accepted().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            service.changePassword(request.getUserId(), request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.accepted().build();
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect");
        }
    }
}
