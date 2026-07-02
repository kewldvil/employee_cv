package com.noc.employee_cv.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_MANAGER = "MANAGER";
    private static final String ROLE_USER = "USER";
    private static final String ROLE_HEAD_OF_BUREAU = "HEAD_OF_BUREAU";

    private static final String EMPLOYEE_CV_READ = "EMPLOYEE_CV_READ";
    private static final String USER_ACCOUNT_MANAGE = "USER_ACCOUNT_MANAGE";
    private static final String USER_RESET_PASSWORD = "USER_RESET_PASSWORD";

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**"
    };

    private static final String[] SWAGGER_URLS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtAuthFilter;
    private final LoginRateLimitFilter loginRateLimitFilter;

    @Value("${app.swagger.enabled:false}")
    private boolean swaggerEnabled;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .headers(headers -> headers
                        .frameOptions(frame -> frame.deny())
                        .contentSecurityPolicy(csp ->
                                csp.policyDirectives("default-src 'self'; object-src 'none'; frame-ancestors 'none'")
                        )
                        .httpStrictTransportSecurity(hsts ->
                                hsts.includeSubDomains(true).maxAgeInSeconds(31536000)
                        )
                        .referrerPolicy(ref ->
                                ref.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER)
                        )
                )

                .authorizeHttpRequests(req -> req

                        // Public endpoints
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers(SWAGGER_URLS).access((authentication, context) ->
                                new org.springframework.security.authorization.AuthorizationDecision(swaggerEnabled)
                        )

                        // Better: avoid public /files/** unless these are truly public
                        .requestMatchers("/photos/**").permitAll()

                        // Static uploaded files should usually be authenticated
                        .requestMatchers("/files/**").authenticated()

                        // Management: method-specific rules FIRST
                        .requestMatchers(HttpMethod.GET, "/api/v1/managements/**")
                        .hasAuthority(EMPLOYEE_CV_READ)

                        .requestMatchers(HttpMethod.POST, "/api/v1/managements/**")
                        .hasAuthority(USER_ACCOUNT_MANAGE)

                        .requestMatchers(HttpMethod.PUT, "/api/v1/managements/user/reset-password/**")
                        .hasAuthority(USER_RESET_PASSWORD)

                        .requestMatchers(HttpMethod.PUT, "/api/v1/managements/**")
                        .hasAuthority(USER_ACCOUNT_MANAGE)

                        .requestMatchers(HttpMethod.DELETE, "/api/v1/managements/**")
                        .hasAuthority(USER_ACCOUNT_MANAGE)

                        // Bureau
                        .requestMatchers("/api/v1/bureau/**")
                        .hasRole(ROLE_ADMIN)

                        // Common protected APIs
                        .requestMatchers(
                                "/api/v1/photo/**",
                                "/api/v1/files/**",
                                "/api/v1/employee/**",
                                "/api/v1/address/**",
                                "/api/v1/enum/**",
                                "/api/v1/general-department/**",
                                "/api/v1/departments/**",
                                "/api/v1/skill/**",
                                "/api/v1/positions/**"
                        )
                        .hasAnyRole(
                                ROLE_ADMIN,
                                ROLE_MANAGER,
                                ROLE_USER,
                                ROLE_HEAD_OF_BUREAU
                        )

                        .anyRequest().denyAll()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("""
                                    {"error":"Unauthorized","status":401}
                                    """);
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("""
                                    {"error":"Forbidden","status":403}
                                    """);
                        })
                )

                .authenticationProvider(authenticationProvider)
                .addFilterBefore(loginRateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
