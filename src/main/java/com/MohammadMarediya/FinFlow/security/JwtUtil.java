package com.MohammadMarediya.FinFlow.security;

import com.MohammadMarediya.FinFlow.exception.TokenExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtil {

    @Value("${Jwt.Secret.Key}")
    private String secretKey;

    @Value("${Jwt.Expiration.Time}")
    private Long jwtExpirationInMillis;

    private Key key;

    @PostConstruct
    public void initialize() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        log.info("JWT secret key initialized");
    }

    public String generateToken(String email, String role) {
        if (email == null || email.isBlank()) {
            log.error("Email is null or blank while generating token");
            throw new IllegalArgumentException("Email cannot be null or blank");
        }


        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.info("JWT token generated for email: {}", email);
        return token;
    }

    private Claims extractClaims(String token) {
        if (token == null || token.isBlank()) {
            log.warn("Attempted to extract claims from a null or blank token");
            throw new BadCredentialsException("Token cannot be null or empty");
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("JWT token has expired: {}", e.getMessage());
            throw new BadCredentialsException("JWT token has expired");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
            throw new BadCredentialsException("Unsupported JWT token");
        } catch (MalformedJwtException e) {
            log.error("Malformed JWT token: {}", e.getMessage());
            throw new BadCredentialsException("Malformed JWT token");
        } catch (SecurityException | IllegalArgumentException e) {
            log.error("Invalid JWT token or signature: {}", e.getMessage());
            throw new BadCredentialsException("Invalid JWT token");
        }
    }

    public String extractEmail(String token) {
        String user = extractClaims(token).getSubject();
        log.info("Extracted Email from token: {}", user);
        return user;
    }

    public Date extractExpirationDate(String token) {
        Date expiration = extractClaims(token).getExpiration();
        log.info("Extracted expiration date from token");
        return expiration;
    }

    public String extractRole(String token) {
        String role = extractClaims(token).get("role", String.class);
        log.info("Extracted role from token: {}", role);
        return role;
    }

    public boolean verifyToken(String token, String expectedUserName) {
        String Email = extractEmail(token);
        Date expiration = extractExpirationDate(token);

        if (Email == null || !Email.equals(expectedUserName)) {
            log.warn("JWT Email mismatch: expected={}, actual={}", expectedUserName, Email);
            throw new BadCredentialsException("Username mismatch in JWT token");
        }

        if (expiration == null || expiration.before(new Date())) {
            log.warn("JWT token is expired for user: {}", Email);
            throw new TokenExpiredException("JWT token has expired");
        }


        log.info("JWT token successfully verified for user: {}", Email);
        return true;
    }
}
