package com.noc.employee_cv;

import com.noc.employee_cv.enums.Role;
import com.noc.employee_cv.models.Skill;
import com.noc.employee_cv.models.User;
import com.noc.employee_cv.provinces.Commune;
import com.noc.employee_cv.provinces.District;
import com.noc.employee_cv.repositories.CommuneRepo;
import com.noc.employee_cv.repositories.DistrictRepo;
import com.noc.employee_cv.repositories.SkillRepo;
import com.noc.employee_cv.repositories.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
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

//    @Bean
//    public CommandLineRunner commandLineRunner(SkillRepo skillRepo) {
//        return args -> {
//            // Retrieve all skills
//            List<Skill> existingSkills = skillRepo.findAll();
//
//            // Update skills if necessary
//            existingSkills.forEach(skill -> {
//                if (skill.getEnabled() == null) {
//                    skill.setEnabled(true); // Assign a default value for null
//                } else if (!skill.getEnabled()) {
//                    skill.setEnabled(true); // Update logic if needed
//                }
//            });
//            // Save all updated skills at once
//            skillRepo.saveAll(existingSkills);
//        };
//    }
//        @Bean
//    public CommandLineRunner commandLineRunner(DistrictRepo districtRepo) {
//        return args -> {
//            // Retrieve all skills
//            List<District> existingDistricts = districtRepo.findAll();
//
//            // Update skills if necessary
//            existingDistricts.forEach(district -> {
//                if (district.getEnabled()== null) {
//                    district.setEnabled(true); // Assign a default value for null
//                }
//            });
//            // Save all updated skills at once
//            districtRepo.saveAll(existingDistricts);
//        };
//    }
//        @Bean
//    public CommandLineRunner commandLineRunner(CommuneRepo communeRepo) {
//        return args -> {
//            // Retrieve all skills
//            List<Commune> existingCommunes = communeRepo.findAll();
//
//            // Update skills if necessary
//            existingCommunes.forEach(commune -> {
//                if (commune.getEnabled()==null) {
//                    commune.setEnabled(true); // Assign a default value for null
//                }
//            });
//            // Save all updated skills at once
//            communeRepo.saveAll(existingCommunes);
//        };
//    }
}
