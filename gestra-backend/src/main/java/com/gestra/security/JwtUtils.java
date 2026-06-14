package com.gestra.security;

import com.gestra.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;

/**
 * Utility class untuk operasi JWT.
 * Membuat, mem-parse, dan memvalidasi JWT token.
 */
@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${gestra.jwt.secret}")
    private String jwtSecret;

    @Value("${gestra.jwt.expiration-ms}")
    private long jwtExpirationMs;

    /** Generate JWT token dari User. */
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .claim("userId", user.getId())
                .claim("nama", user.getNamaLengkap())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    /** Ekstrak email (subject) dari token. */
    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /** Validasi token: signature, expiration, format. */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            log.warn("JWT tidak valid: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("JWT kadaluarsa: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT tidak didukung: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT kosong: {}", e.getMessage());
        }
        return false;
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        byte[] paddedKey = Arrays.copyOf(keyBytes, Math.max(keyBytes.length, 32));
        return Keys.hmacShaKeyFor(paddedKey);
    }
}
