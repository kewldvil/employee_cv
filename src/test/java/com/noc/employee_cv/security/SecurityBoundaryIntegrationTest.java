package com.noc.employee_cv.security;

import com.noc.employee_cv.model.Role;
import com.noc.employee_cv.model.User;
import com.noc.employee_cv.repository.RoleRepo;
import com.noc.employee_cv.repository.UserRepo;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityBoundaryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void loginRemainsPublic() throws Exception {
        mockMvc.perform(post("/api/v1/auth/authenticate/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void anonymousCannotRegisterOrResetPasswords() throws Exception {
        mockMvc.perform(post("/api/v1/auth/authenticate/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/v1/auth/authenticate/forget-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/v1/auth/authenticate/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void anonymousCannotReadUploadedContent() throws Exception {
        mockMvc.perform(get("/photos/example.png"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/files/example.pdf"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refreshTokenRotatesAndReplayRevokesTheTokenFamily() throws Exception {
        Role role = roleRepo.findByName("TEST_REFRESH")
                .orElseGet(() -> roleRepo.save(Role.builder().name("TEST_REFRESH").build()));
        String username = "refresh-user-" + System.nanoTime();
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode("Password1!"))
                .firstname("Refresh")
                .lastname("Test")
                .email(username + "@example.invalid")
                .enabled(true)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        user.replaceRole(role);
        userRepo.save(user);

        String firstCookie = mockMvc.perform(post("/api/v1/auth/authenticate/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"Password1!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andReturn().getResponse().getHeader("Set-Cookie");

        String firstToken = cookieValue(firstCookie);
        String secondCookie = mockMvc.perform(post("/api/v1/auth/authenticate/refresh")
                        .cookie(new Cookie("refresh_token", firstToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andReturn().getResponse().getHeader("Set-Cookie");

        String secondToken = cookieValue(secondCookie);
        mockMvc.perform(post("/api/v1/auth/authenticate/refresh")
                        .cookie(new Cookie("refresh_token", firstToken)))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/v1/auth/authenticate/refresh")
                        .cookie(new Cookie("refresh_token", secondToken)))
                .andExpect(status().isUnauthorized());
    }

    private String cookieValue(String setCookie) {
        return setCookie.substring("refresh_token=".length(), setCookie.indexOf(';'));
    }
}
