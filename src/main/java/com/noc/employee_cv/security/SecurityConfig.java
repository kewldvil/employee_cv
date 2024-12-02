package com.noc.employee_cv.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.noc.employee_cv.enums.Permission.ADMIN_READ;
import static com.noc.employee_cv.enums.Permission.MANAGER_READ;
import static com.noc.employee_cv.enums.Role.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
//            "/photos/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};
    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtAuthFilter;
    // Define common roles and authorities
    String[] COMMON_ROLES = {ADMIN.name(), MANAGER.name(), USER.name(), HEAD_OF_BUREAU.name()};
    String[] ADMIN_ONLY = {ADMIN.name()};
    String[] READ_AUTHORITIES = {ADMIN_READ.name(), MANAGER_READ.name(), HEAD_OF_BUREAU.name()};
    String[] ADMIN_READ_ONLY = {ADMIN_READ.name()};
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        // Permit all to specific paths
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers("/photos/**").permitAll()

                        // Common roles for multiple endpoints
                        .requestMatchers("/api/v1/photo/**", "/api/v1/files/**", "/api/v1/employee/**", "/api/v1/address/**", "/api/v1/enum/**")
                        .hasAnyRole(COMMON_ROLES)
                        .requestMatchers("/api/v1/general-department/**", "/api/v1/department/**", "/api/v1/skill/**", "/api/v1/positions/**")
                        .hasAnyRole(COMMON_ROLES)

                        // Bureau endpoint restricted to ADMIN only
                        .requestMatchers("/api/v1/bureau").hasAnyRole(ADMIN_ONLY)

                        // Management endpoint role-based access
                        .requestMatchers("/api/v1/managements/**")
                        .hasAnyRole(ADMIN.name(), MANAGER.name(), HEAD_OF_BUREAU.name())

                        // Management endpoint with method-specific authority checks
                        .requestMatchers(GET, "/api/v1/managements/**").hasAnyAuthority(READ_AUTHORITIES)
                        .requestMatchers(POST, "/api/v1/managements/**").hasAnyAuthority(ADMIN_READ_ONLY)
                        .requestMatchers(PUT, "/api/v1/managements/**").hasAnyAuthority(ADMIN_READ_ONLY)
                        .requestMatchers(DELETE, "/api/v1/managements/**").hasAnyAuthority(ADMIN_READ_ONLY)

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }
//SIMPLE WAY EXPLANATION
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        // Enable CORS with default settings
//        http.cors(Customizer.withDefaults());
//
//        // Disable CSRF protection
//        http.csrf(AbstractHttpConfigurer::disable);
//
//        // Configure authorization for different URL patterns
//        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests = http.authorizeRequests();
//
//        // Allow all requests to URLs in the whitelist and /photos/**
//        authorizeRequests.requestMatchers(WHITE_LIST_URL).permitAll();
//        authorizeRequests.requestMatchers("/photos/**").permitAll();
//
//        // Require specific roles for /api/v1/photo/** and other similar endpoints
//        authorizeRequests
//                .requestMatchers("/api/v1/photo/**", "/api/v1/files/**", "/api/v1/employee/**", "/api/v1/address/**", "/api/v1/enum/**")
//                .hasAnyRole(ADMIN.name(), MANAGER.name(), USER.name());
//
//        // Require ADMIN or MANAGER roles for /api/v1/managements/**
//        authorizeRequests.requestMatchers("/api/v1/managements/**").hasAnyRole(ADMIN.name(), MANAGER.name());
//
//        // Require specific authorities for different HTTP methods on /api/v1/managements/**
//        authorizeRequests.requestMatchers(HttpMethod.GET, "/api/v1/managements/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name());
//        authorizeRequests.requestMatchers(HttpMethod.POST, "/api/v1/managements/**").hasAnyAuthority(ADMIN_READ.name());
//        authorizeRequests.requestMatchers(HttpMethod.PUT, "/api/v1/managements/**").hasAnyAuthority(ADMIN_READ.name());
//        authorizeRequests.requestMatchers(HttpMethod.DELETE, "/api/v1/managements/**").hasAnyAuthority(ADMIN_READ.name());
//
//        // Require authentication for any other requests
//        authorizeRequests.anyRequest().authenticated();
//
//        // Configure session management as stateless
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        // Set the custom authentication provider
//        http.authenticationProvider(authenticationProvider);
//
//        // Add the JWT authentication filter before the UsernamePasswordAuthenticationFilter
//        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        // Build and return the configured SecurityFilterChain
//        return http.build();
//    }

}
