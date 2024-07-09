package com.noc.employee_cv;

import com.noc.employee_cv.enums.Role;
import com.noc.employee_cv.models.User;
import com.noc.employee_cv.repositories.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
@EnableAsync
public class EmployeeCvApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeCvApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(UserRepo userRepo,PasswordEncoder passwordEncoder) {
//
//        return args -> {
//            // Retrieve existing users
//            Iterable<User> existingUsers = userRepo.findAll();
//            // Update users with new email addresses
//            existingUsers.forEach(user -> {
//                // Update user with new email address
//                String newPassword = "12345678";
//                user.setPassword(passwordEncoder.encode(newPassword));
//                user.setRole(Role.USER);
//                userRepo.save(user);
//            });
//        };
//    }

//    @Bean
//    public CommandLineRunner runner(RoleEntityRepo roleRepo) {
//        return args -> {
//            if (roleRepo.findByName("USER").isEmpty()) {
//                roleRepo.save(
//                        Role.builder()
//                                .name("USER").build()
//                );
//            }
//            if (roleRepo.findByName("ADMIN").isEmpty()) {
//                roleRepo.save(
//                        Role.builder()
//                                .name("ADMIN").build()
//                );
//            }
//            if (roleRepo.findByName("MANAGER").isEmpty()) {
//                roleRepo.save(
//                        Role.builder()
//                                .name("MANAGER").build()
//                );
//            }
//        };
//    }
}
