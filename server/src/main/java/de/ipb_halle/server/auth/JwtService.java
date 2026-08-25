package de.ipb_halle.server.auth;

import de.ipb_halle.server.postgre.models.UserEntity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

        private final SecretKey secretKey;
        private final long expirationSeconds;

        public JwtService(
                        @Value("${jwt.secret}") String secret,
                        @Value("${jwt.expiration-seconds:86400}") long expirationSeconds) {

                this.secretKey = Keys.hmacShaKeyFor(
                                secret.getBytes(StandardCharsets.UTF_8));

                this.expirationSeconds = expirationSeconds;
        }

        public String generateToken(UserEntity user) {

                Instant now = Instant.now();

                return Jwts.builder()
                                .subject(user.getOrcid())
                                .claim("userId", user.getId())
                                .claim("role", user.getRole().name())
                                .issuedAt(Date.from(now))
                                .expiration(Date.from(
                                                now.plusSeconds(expirationSeconds)))
                                .signWith(secretKey)
                                .compact();
        }
}