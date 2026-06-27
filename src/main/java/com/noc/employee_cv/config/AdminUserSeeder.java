package com.noc.employee_cv.config;

import com.noc.employee_cv.enums.Role;
import com.noc.employee_cv.model.User;
import com.noc.employee_cv.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class AdminUserSeeder implements ApplicationRunner {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int RANDOM_PASSWORD_LENGTH = 8;
    private static final String PASSWORD_UPPERCASE = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String PASSWORD_LOWERCASE = "abcdefghijkmnopqrstuvwxyz";
    private static final String PASSWORD_DIGITS = "23456789";
    private static final String PASSWORD_SYMBOLS = "!@#$%^&*()-_=+[]{}";
    private static final String PASSWORD_ALLOWED_CHARACTERS =
            PASSWORD_UPPERCASE + PASSWORD_LOWERCASE + PASSWORD_DIGITS + PASSWORD_SYMBOLS;

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final boolean enabled;
    private final boolean resetNonAdminPasswords;
    private final String adminUsername;
    private final String adminPassword;
    private final String adminEmail;
    private final String adminFirstname;
    private final String adminLastname;

    public AdminUserSeeder(
            UserRepo userRepo,
            PasswordEncoder passwordEncoder,
            @Value("${app.seed.enabled:true}") boolean enabled,
            @Value("${app.seed.reset-non-admin-passwords:false}") boolean resetNonAdminPasswords,
            @Value("${app.seed.admin.username}") String adminUsername,
            @Value("${app.seed.admin.password}") String adminPassword,
            @Value("${app.seed.admin.email:admin@local.invalid}") String adminEmail,
            @Value("${app.seed.admin.firstname:Admin}") String adminFirstname,
            @Value("${app.seed.admin.lastname:User}") String adminLastname
    ) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.enabled = enabled;
        this.resetNonAdminPasswords = resetNonAdminPasswords;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.adminEmail = adminEmail;
        this.adminFirstname = adminFirstname;
        this.adminLastname = adminLastname;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!enabled) {
            return;
        }

        requireText(adminUsername, "ADMIN_USERNAME");
        requireText(adminPassword, "ADMIN_PASSWORD");

        User admin = userRepo.findByUsername(adminUsername)
                .map(this::updateAdminUser)
                .orElseGet(this::createAdminUser);

        userRepo.save(admin);

        if (!resetNonAdminPasswords) {
            log.info("Admin user '{}' is ready. Non-admin password reset is disabled.", adminUsername);
            return;
        }

        List<User> resetUsers = userRepo.findAllExceptUsername(adminUsername);
        log.warn("Resetting passwords for {} non-admin user(s). This may take some time.", resetUsers.size());
        resetUsers.forEach(user -> user.setPassword(passwordEncoder.encode(generateRandomPassword())));
        userRepo.saveAll(resetUsers);

        log.info("Admin user '{}' is ready. Reset passwords for {} non-admin user(s).", adminUsername, resetUsers.size());
    }

    private User createAdminUser() {
        return User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .firstname(adminFirstname)
                .lastname(adminLastname)
                .email(adminEmail)
                .accountLocked(false)
                .failedLoginAttempts(0)
                .enabled(true)
                .role(Role.ADMIN)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
    }

    private User updateAdminUser(User admin) {
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setFirstname(adminFirstname);
        admin.setLastname(adminLastname);
        admin.setEmail(adminEmail);
        admin.setAccountLocked(false);
        admin.setFailedLoginAttempts(0);
        admin.setAccountLockedAt(null);
        admin.setEnabled(true);
        admin.setRole(Role.ADMIN);
        return admin;
    }

    private void requireText(String value, String envName) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalStateException(envName + " must be configured in the active profile .env file");
        }
    }

    private String generateRandomPassword() {
        List<Character> passwordCharacters = new ArrayList<>(RANDOM_PASSWORD_LENGTH);
        passwordCharacters.add(randomCharacter(PASSWORD_UPPERCASE));
        passwordCharacters.add(randomCharacter(PASSWORD_LOWERCASE));
        passwordCharacters.add(randomCharacter(PASSWORD_DIGITS));
        passwordCharacters.add(randomCharacter(PASSWORD_SYMBOLS));

        while (passwordCharacters.size() < RANDOM_PASSWORD_LENGTH) {
            passwordCharacters.add(randomCharacter(PASSWORD_ALLOWED_CHARACTERS));
        }

        Collections.shuffle(passwordCharacters, SECURE_RANDOM);

        StringBuilder password = new StringBuilder(RANDOM_PASSWORD_LENGTH);
        for (Character character : passwordCharacters) {
            password.append(character);
        }
        return password.toString();
    }

    private char randomCharacter(String characters) {
        return characters.charAt(SECURE_RANDOM.nextInt(characters.length()));
    }
}
