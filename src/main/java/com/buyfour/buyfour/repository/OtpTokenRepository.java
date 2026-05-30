package com.buyfour.buyfour.repository;

import com.buyfour.buyfour.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {

    Optional<OtpToken> findTopByEmailOrderByExpiryTimeDesc(String email);

    void deleteByEmail(String email);
}
