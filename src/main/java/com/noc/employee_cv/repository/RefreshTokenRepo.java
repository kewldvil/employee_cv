package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.RefreshToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Modifying
    @Query("UPDATE RefreshToken token SET token.revokedAt = :now WHERE token.user.id = :userId AND token.revokedAt IS NULL")
    int revokeAllByUserId(@Param("userId") Integer userId, @Param("now") LocalDateTime now);

    long deleteByExpiresAtBefore(LocalDateTime cutoff);
}
