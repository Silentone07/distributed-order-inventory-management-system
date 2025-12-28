package com.inventorysystem.auth.service.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY =
            "my-secret-key-my-secret-key-my-secret-key"; // min 32 chars

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public static String generateToken(String userName) {
         return Jwts.builder().setSubject(userName).
                 setIssuedAt(new Date()).
                 setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                 .signWith(getSigningKey())
                 .compact();
    }
}
