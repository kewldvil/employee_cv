package com.noc.employee_cv.authentication;

import com.noc.employee_cv.email.EmailService;
import com.noc.employee_cv.email.EmailTemplateName;
import com.noc.employee_cv.enums.Role;
import com.noc.employee_cv.models.Token;
import com.noc.employee_cv.models.User;
import com.noc.employee_cv.repositories.StorageRepo;
import com.noc.employee_cv.repositories.TokenRepo;
import com.noc.employee_cv.repositories.UserRepo;
import com.noc.employee_cv.security.JwtService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    @Autowired
    private UserRepo userRepo;
    private final EmailService emailService;

    @Value("${activation_url}")
    private String activationUrl;


    public void register(RegistrationRequest request) throws MessagingException {
//        var userRole = roleRepo.findByName(request.getRoles())
//                // todo - better exception handling
//                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));
        var user = User.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(true)
//                .roles(Set.of(userRole))
                .role(Role.valueOf(request.getRole()))
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        System.out.println(user.toString());
        userRepo.save(user);
//        sendValidationEmail(user);

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
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = (User) auth.getPrincipal();
//        var user = User.builder()
//                .id(loggedInUser.getId())
//                .firstname(loggedInUser.getFirstname())
//                .lastname(loggedInUser.getLastname())
//                .role(loggedInUser.getRole())
//                .build();
        claims.put("firstname", user.getFirstname());
        claims.put("lastname", user.getLastname());
        claims.put("id",user.getId());
        claims.put("role", user.getRole());
        var jwtToken = jwtService.generateToken(claims, user);
//        System.out.println(jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
//                .user(user)
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
    public void updatePassword(ForgetPasswordRequest request)  throws MessagingException{
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
}
