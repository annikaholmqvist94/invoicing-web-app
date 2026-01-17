package org.example.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    // Skapar en säker nyckel för HS256
    private final SecretKey key = Keys.hmacShaKeyFor("en-valdigt-lang-och-super-hemlig-nyckel-12345".getBytes());
    private final long EXPIRATION_TIME = 86400000; // 24h

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }
}