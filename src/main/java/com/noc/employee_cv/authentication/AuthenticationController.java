package com.noc.employee_cv.authentication;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/authenticate")
@RequiredArgsConstructor
//@Tag(name="Authentication")
public class AuthenticationController {
    private static final String REFRESH_COOKIE = "refresh_token";
    private final AuthenticationService service;
    @Value("${jwt.refresh-expiration:P7D}")
    private java.time.Duration refreshExpiration;
    @Value("${jwt.refresh-cookie-secure:true}")
    private boolean secureRefreshCookie;
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
        AuthenticationResponse response = service.authenticate(request);
        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.SET_COOKIE, refreshCookie(response.getRefreshToken()).toString())
                .body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(
            @CookieValue(name = REFRESH_COOKIE, required = false) String refreshToken
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadCredentialsException("Refresh token is required");
        }
        AuthenticationResponse response = service.refresh(refreshToken);
        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.SET_COOKIE, refreshCookie(response.getRefreshToken()).toString())
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = REFRESH_COOKIE, required = false) String refreshToken
    ) {
        service.logout(refreshToken);
        ResponseCookie expiredCookie = ResponseCookie.from(REFRESH_COOKIE, "")
                .httpOnly(true)
                .secure(secureRefreshCookie)
                .sameSite("Strict")
                .path("/api/v1/auth/authenticate")
                .maxAge(0)
                .build();
        return ResponseEntity.noContent()
                .header(org.springframework.http.HttpHeaders.SET_COOKIE, expiredCookie.toString())
                .build();
    }

    private ResponseCookie refreshCookie(String value) {
        return ResponseCookie.from(REFRESH_COOKIE, value)
                .httpOnly(true)
                .secure(secureRefreshCookie)
                .sameSite("Strict")
                .path("/api/v1/auth/authenticate")
                .maxAge(refreshExpiration)
                .build();
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
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal com.noc.employee_cv.model.User currentUser,
                                                 @RequestBody @Valid ChangePasswordRequest request) {
        try {
            service.changePassword(currentUser.getId(), request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.accepted().build();
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect");
        }
    }
}
