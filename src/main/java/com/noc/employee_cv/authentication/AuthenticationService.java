package com.noc.employee_cv.authentication;

import com.noc.employee_cv.email.EmailService;
import com.noc.employee_cv.email.EmailTemplateName;
import com.noc.employee_cv.enums.Role;
import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.models.Token;
import com.noc.employee_cv.models.User;
import com.noc.employee_cv.repositories.EmployeeRepo;
import com.noc.employee_cv.repositories.TokenRepo;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.security.JwtService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepo tokenRepo;
    private final EmployeeRepo employeeRepo;
    private UserRepo userRepo;
    private final EmailService emailService;

    @Value("${activation_url}")
    private String activationUrl;


    public void register(RegistrationRequest request) throws MessagingException {
        // Check if the username already exists
        if (userRepo.existsByUsername(request.getUsername())) {
            // Throw a custom exception or handle the error accordingly
            throw new IllegalArgumentException("Username '" + request.getUsername() + "' is already taken.");
        }

        // Create the user object
        var user = User.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(true)
                .role(Role.valueOf(request.getRole()))
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

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
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomMix = secureRandom.nextInt(characters.length());//0..9
            sb.append(characters.charAt(randomMix));

        }
        return sb.toString();
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String newToken = generateAndSaveToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken, "Account Activation"
        );
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = (User) authentication.getPrincipal();
        var claims = new HashMap<String, Object>();
        Employee employee = employeeRepo.findByUserId(user.getId());

        // Add user details to claims
        claims.put("firstname", user.getFirstname());
        claims.put("lastname", user.getLastname());
        claims.put("id", user.getId());
        claims.put("role", user.getRole());

        // Add department ID if available
        if (employee != null && employee.getDepartment() != null) {
            claims.put("depId", employee.getDepartment().getId());
        } else {
            System.out.println("Employee or Department is null");
        }

        var jwtToken = jwtService.generateToken(claims, user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    //    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepo.findByToken(token)
                // todo exception has to be defined
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiredAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }

        var user = userRepo.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepo.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepo.save(savedToken);
    }

    public User getCurrentUserName(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        return (User) auth.getPrincipal();
    }

    @Transactional
    public void forgetPassword(ForgetPasswordRequest request) throws MessagingException {
        System.out.println("update password");
        System.out.println(request.toString());
        // Preparing parameters for delimiter checks
        String delimiterPhoneNumberStart = request.getPhoneNumber() + "/%";
        String phoneNumberDelimiterEnd = "%/" + request.getPhoneNumber();
        try {
            var user = userRepo.findByUsernameAndPhoneNumberAndDateOfBirth(request.getUsername(), request.getPhoneNumber(), delimiterPhoneNumberStart, phoneNumberDelimiterEnd, request.getDateOfBirth())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepo.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update password: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void changePassword(Integer userId, String oldPassword, String newPassword) throws ChangeSetPersister.NotFoundException {
        try {
            User user = userRepo.findById(userId)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);

            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new IncorrectPasswordException("Current password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to change password: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void resetPassword(Integer userId) throws ChangeSetPersister.NotFoundException {
        try {
            User user = userRepo.findById(userId).orElseThrow(ChangeSetPersister.NotFoundException::new);
            user.setPassword(passwordEncoder.encode("12345678"));
            userRepo.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset password: " + e.getMessage(), e);
        }
    }


    @Transactional
    public void updateUserByEnabled(Integer userId, boolean enabled) {
        userRepo.updateUserByEnabled(userId, enabled);
    }
}
