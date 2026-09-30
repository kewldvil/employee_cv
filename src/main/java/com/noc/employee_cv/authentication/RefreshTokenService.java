package com.noc.employee_cv.authentication;

import com.noc.employee_cv.model.RefreshToken;
import com.noc.employee_cv.model.User;
import com.noc.employee_cv.repository.RefreshTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int TOKEN_BYTES = 32;

    private final RefreshTokenRepo refreshTokenRepo;

    @Value("${jwt.refresh-expiration:P7D}")
    private Duration refreshExpiration;

    @Transactional
    public IssuedRefreshToken issue(User user) {
        String rawToken = randomToken();
        LocalDateTime now = LocalDateTime.now();
        RefreshToken storedToken = RefreshToken.builder()
                .tokenHash(hash(rawToken))
                .user(user)
                .createdAt(now)
                .expiresAt(now.plus(refreshExpiration))
                .build();
        refreshTokenRepo.save(storedToken);
        return new IssuedRefreshToken(rawToken, refreshExpiration);
    }

    @Transactional(noRollbackFor = BadCredentialsException.class)
    public RotatedRefreshToken rotate(String rawToken) {
        RefreshToken storedToken = refreshTokenRepo.findByTokenHash(hash(rawToken))
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
        LocalDateTime now = LocalDateTime.now();

        if (storedToken.getRevokedAt() != null) {
            refreshTokenRepo.revokeAllByUserId(storedToken.getUser().getId(), now);
            throw new BadCredentialsException("Refresh token has already been used");
        }
        if (!storedToken.getExpiresAt().isAfter(now)) {
            storedToken.setRevokedAt(now);
            throw new BadCredentialsException("Refresh token has expired");
        }
        User user = storedToken.getUser();
        if (!user.isEnabled() || !user.isAccountNonLocked()) {
            refreshTokenRepo.revokeAllByUserId(user.getId(), now);
            throw new BadCredentialsException("Account is unavailable");
        }

        storedToken.setRevokedAt(now);
        return new RotatedRefreshToken(user, issue(user));
    }

    @Transactional
    public void revoke(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            return;
        }
        refreshTokenRepo.findByTokenHash(hash(rawToken))
                .filter(token -> token.getRevokedAt() == null)
                .ifPresent(token -> token.setRevokedAt(LocalDateTime.now()));
    }

    @Transactional
    public void revokeAll(User user) {
        refreshTokenRepo.revokeAllByUserId(user.getId(), LocalDateTime.now());
    }

    private String randomToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }

    public record IssuedRefreshToken(String value, Duration lifetime) {}
    public record RotatedRefreshToken(User user, IssuedRefreshToken refreshToken) {}
}
