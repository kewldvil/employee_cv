package com.noc.employee_cv.config;

import com.noc.employee_cv.model.Permission;
import com.noc.employee_cv.model.Role;
import com.noc.employee_cv.model.User;
import com.noc.employee_cv.repository.PermissionRepo;
import com.noc.employee_cv.repository.RoleRepo;
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
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String MANAGER_ROLE = "MANAGER";
    private static final String USER_ROLE = "USER";
    private static final String HEAD_OF_BUREAU_ROLE = "HEAD_OF_BUREAU";

    private static final String EMPLOYEE_CV_READ = "EMPLOYEE_CV_READ";
    private static final String EMPLOYEE_CV_CREATE = "EMPLOYEE_CV_CREATE";
    private static final String EMPLOYEE_CV_UPDATE = "EMPLOYEE_CV_UPDATE";
    private static final String EMPLOYEE_CV_DELETE = "EMPLOYEE_CV_DELETE";
    private static final String USER_ACCOUNT_MANAGE = "USER_ACCOUNT_MANAGE";
    private static final String USER_RESET_PASSWORD = "USER_RESET_PASSWORD";
    private static final String ORGANIZATION_MANAGE = "ORGANIZATION_MANAGE";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int RANDOM_PASSWORD_LENGTH = 8;
    private static final String PASSWORD_UPPERCASE = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String PASSWORD_LOWERCASE = "abcdefghijkmnopqrstuvwxyz";
    private static final String PASSWORD_DIGITS = "23456789";
    private static final String PASSWORD_SYMBOLS = "!@#$%^&*()-_=+[]{}";
    private static final String PASSWORD_ALLOWED_CHARACTERS =
            PASSWORD_UPPERCASE + PASSWORD_LOWERCASE + PASSWORD_DIGITS + PASSWORD_SYMBOLS;

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PermissionRepo permissionRepo;
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
            RoleRepo roleRepo,
            PermissionRepo permissionRepo,
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
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
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

        Role adminRole = seedRolesAndPermissions();

        User admin = userRepo.findByUsername(adminUsername)
                .map(user -> updateAdminUser(user, adminRole))
                .orElseGet(() -> createAdminUser(adminRole));

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

    private User createAdminUser(Role adminRole) {
        User admin = User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .firstname(adminFirstname)
                .lastname(adminLastname)
                .email(adminEmail)
                .accountLocked(false)
                .failedLoginAttempts(0)
                .enabled(true)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        admin.replaceRole(adminRole);
        return admin;
    }

    private User updateAdminUser(User admin, Role adminRole) {
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setFirstname(adminFirstname);
        admin.setLastname(adminLastname);
        admin.setEmail(adminEmail);
        admin.setAccountLocked(false);
        admin.setFailedLoginAttempts(0);
        admin.setAccountLockedAt(null);
        admin.setEnabled(true);
        admin.replaceRole(adminRole);
        return admin;
    }

    private Role seedRolesAndPermissions() {
        Permission employeeCvRead = savePermission(EMPLOYEE_CV_READ, "Read employee CV records and dashboard statistics");
        Permission employeeCvCreate = savePermission(EMPLOYEE_CV_CREATE, "Create employee CV records");
        Permission employeeCvUpdate = savePermission(EMPLOYEE_CV_UPDATE, "Update employee CV records");
        Permission employeeCvDelete = savePermission(EMPLOYEE_CV_DELETE, "Delete employee CV records");
        Permission userAccountManage = savePermission(USER_ACCOUNT_MANAGE, "Manage user account status, password, profile, and department");
        Permission userResetPassword = savePermission(USER_RESET_PASSWORD, "Reset user passwords");
        Permission organizationManage = savePermission(ORGANIZATION_MANAGE, "Manage departments, bureaus, positions, skills, and address lookup data");

        saveRole(USER_ROLE, employeeCvRead);
        saveRole(HEAD_OF_BUREAU_ROLE, employeeCvRead, employeeCvUpdate, userResetPassword);
        saveRole(MANAGER_ROLE, employeeCvRead, employeeCvCreate, employeeCvUpdate);
        return saveRole(ADMIN_ROLE, employeeCvRead, employeeCvCreate, employeeCvUpdate, employeeCvDelete, userAccountManage, userResetPassword, organizationManage);
    }

    private Permission savePermission(String name, String description) {
        Permission permission = permissionRepo.findByName(name)
                .orElseGet(() -> Permission.builder()
                        .name(name)
                        .description(description)
                        .build());
        permission.setDescription(description);
        return permissionRepo.save(permission);
    }

    private Role saveRole(String name, Permission... permissions) {
        Role role = roleRepo.findByName(name)
                .orElseGet(() -> Role.builder()
                        .name(name)
                        .build());
        role.getPermissions().clear();
        role.getPermissions().addAll(List.of(permissions));
        return roleRepo.save(role);
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
