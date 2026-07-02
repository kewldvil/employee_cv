package com.noc.employee_cv.authentication;

import com.noc.employee_cv.email.EmailService;
import com.noc.employee_cv.model.Employee;
import com.noc.employee_cv.model.Role;
import com.noc.employee_cv.model.Token;
import com.noc.employee_cv.model.User;
import com.noc.employee_cv.repository.EmployeeRepo;
import com.noc.employee_cv.repository.RoleRepo;
import com.noc.employee_cv.repository.TokenRepo;
import com.noc.employee_cv.repository.UserRepo;
import com.noc.employee_cv.security.JwtService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class AuthenticationService {
    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int TEMPORARY_PASSWORD_LENGTH = 8;
    private static final String PASSWORD_UPPERCASE = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String PASSWORD_LOWERCASE = "abcdefghijkmnopqrstuvwxyz";
    private static final String PASSWORD_DIGITS = "23456789";
    private static final String PASSWORD_SYMBOLS = "!@#$%^&*()-_=+[]{}";
    private static final String PASSWORD_ALLOWED_CHARACTERS =
            PASSWORD_UPPERCASE + PASSWORD_LOWERCASE + PASSWORD_DIGITS + PASSWORD_SYMBOLS;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepo tokenRepo;
    private final EmployeeRepo employeeRepo;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final EmailService emailService;

//    @Value("${activation_url}")
//    private String activationUrl;


    public void register(@Valid @NotNull RegistrationRequest request) throws MessagingException {
        // Check if the username already exists
        if (userRepo.existsByUsername(request.getUsername())) {
            // Throw a custom exception or handle the error accordingly
            throw new IllegalArgumentException("Username '" + request.getUsername() + "' is already taken.");
        }

        Role role = findRole(request.getRole());

        // Create the user object
        var user = User.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .failedLoginAttempts(0)
                .enabled(true)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        user.replaceRole(role);

        // Save the user to the repository
        userRepo.save(user);

        // Optionally send validation email
        // sendValidationEmail(user);
    }


    private String generateAndSaveToken(User user) {
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepo.save(token);
        return generatedToken;


    }

    public String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomMix = SECURE_RANDOM.nextInt(characters.length());//0..9
            sb.append(characters.charAt(randomMix));

        }
        return sb.toString();
    }

//    private void sendValidationEmail(User user) throws MessagingException {
//        String newToken = generateAndSaveToken(user);
//        emailService.sendEmail(
//                user.getEmail(),
//                user.getFullName(),
//                EmailTemplateName.ACTIVATE_ACCOUNT,
//                activationUrl,
//                newToken, "Account Activation"
//        );
//    }

    @Transactional(dontRollbackOn = {BadCredentialsException.class, LockedException.class})
    public AuthenticationResponse authenticate(@Valid @NotNull AuthenticationRequest request) {
        userRepo.findByUsername(request.getUsername()).ifPresent(user -> {
            if (user.isAccountLocked()) {
                throw new LockedException("Account is locked after too many failed login attempts");
            }
            if (!user.isEnabled()) {
                throw new DisabledException("Account is disabled");
            }
        });

        var authentication = authenticateCredentials(request);

        var user = (User) authentication.getPrincipal();
        resetFailedLoginAttempts(user);
        var claims = new HashMap<String, Object>();
        Employee employee = employeeRepo.findByUserId(user.getId());

        // Add user details to claims
        claims.put("firstname", user.getFirstname());
        claims.put("lastname", user.getLastname());
        claims.put("id", user.getId());
        claims.put("role", user.getRoleName());

        // Add department ID if available
        if (employee != null && employee.getDepartment() != null) {
            claims.put("depId", employee.getDepartment().getId());
        } else {
        }

        var jwtToken = jwtService.generateToken(claims, user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private org.springframework.security.core.Authentication authenticateCredentials(AuthenticationRequest request) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException exception) {
            boolean accountLocked = recordFailedLoginAttempt(request.getUsername());
            if (accountLocked) {
                throw new LockedException("Account is locked after too many failed login attempts");
            }
            throw new BadCredentialsException("Invalid username or password");
        } catch (AuthenticationException exception) {
            throw exception;
        }
    }

    private boolean recordFailedLoginAttempt(String username) {
        return userRepo.findByUsernameForUpdate(username).map(user -> {
            int failedAttempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(failedAttempts);
            if (failedAttempts >= MAX_FAILED_LOGIN_ATTEMPTS) {
                user.setAccountLocked(true);
                user.setEnabled(false);
                user.setAccountLockedAt(LocalDateTime.now());
            }
            userRepo.save(user);
            return user.isAccountLocked();
        }).orElse(false);
    }

    private void resetFailedLoginAttempts(User user) {
        if (user.getFailedLoginAttempts() == 0 && !user.isAccountLocked() && user.getAccountLockedAt() == null) {
            return;
        }
        unlockUser(user);
        userRepo.save(user);
    }

    private void unlockUser(User user) {
        user.setFailedLoginAttempts(0);
        user.setAccountLocked(false);
        user.setAccountLockedAt(null);

    }

    private Role findRole(String roleName) {
        return roleRepo.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role specified"));
    }


    //    @Transactional
    public void activateAccount(@NotBlank(message = "token is required") String token) throws MessagingException {
        Token savedToken = tokenRepo.findByToken(token)
                // todo exception has to be defined
                .orElseThrow(() -> new RuntimeException("Invalid token"));
//        if (LocalDateTime.now().isAfter(savedToken.getExpiredAt())) {
//            sendValidationEmail(savedToken.getUser());
//            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
//        }

        var user = userRepo.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepo.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepo.save(savedToken);
    }

    public User getCurrentUserName(@Valid @NotNull AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        return (User) auth.getPrincipal();
    }

    @Transactional
    public void forgetPassword(@Valid @NotNull ForgetPasswordRequest request) throws MessagingException {
        // Preparing parameters for delimiter checks
        String delimiterPhoneNumberStart = request.getPhoneNumber() + "/%";
        String phoneNumberDelimiterEnd = "%/" + request.getPhoneNumber();
        try {
            var user = userRepo.findByUsernameAndPhoneNumberAndDateOfBirth(request.getUsername(), request.getPhoneNumber(), delimiterPhoneNumberStart, phoneNumberDelimiterEnd, request.getDateOfBirth())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setPassword(passwordEncoder.encode(request.getPassword()));
            unlockUser(user);
            userRepo.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update password: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void changePassword(
            @NotNull(message = "userId is required") Integer userId,
            @NotBlank(message = "oldPassword is required") String oldPassword,
            @NotBlank(message = "newPassword is required")
            @Size(min = 8, max = 72, message = "newPassword must be between 8 and 72 characters") String newPassword
    ) throws ChangeSetPersister.NotFoundException {
        try {
            User user = userRepo.findById(userId)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);

            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new IncorrectPasswordException("Current password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            unlockUser(user);
            userRepo.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to change password: " + e.getMessage(), e);
        }
    }

    @Transactional
    public String resetPassword(@NotNull(message = "userId is required") Integer userId) throws ChangeSetPersister.NotFoundException {
        User user = userRepo.findById(userId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        String temporaryPassword = generateStrongPassword();
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        unlockUser(user);
        userRepo.save(user);
        return temporaryPassword;
    }

    private String generateStrongPassword() {
        List<Character> passwordCharacters = new ArrayList<>(TEMPORARY_PASSWORD_LENGTH);
        passwordCharacters.add(randomCharacter(PASSWORD_UPPERCASE));
        passwordCharacters.add(randomCharacter(PASSWORD_LOWERCASE));
        passwordCharacters.add(randomCharacter(PASSWORD_DIGITS));
        passwordCharacters.add(randomCharacter(PASSWORD_SYMBOLS));

        while (passwordCharacters.size() < TEMPORARY_PASSWORD_LENGTH) {
            passwordCharacters.add(randomCharacter(PASSWORD_ALLOWED_CHARACTERS));
        }

        Collections.shuffle(passwordCharacters, SECURE_RANDOM);

        StringBuilder password = new StringBuilder(TEMPORARY_PASSWORD_LENGTH);
        for (Character character : passwordCharacters) {
            password.append(character);
        }
        return password.toString();
    }

    private char randomCharacter(String characters) {
        return characters.charAt(SECURE_RANDOM.nextInt(characters.length()));
    }


    @Transactional
    public void updateUserByEnabled(@NotNull(message = "userId is required") Integer userId, boolean enabled) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(enabled);
        if (enabled) {
            unlockUser(user);
        }
        userRepo.save(user);
    }
}
