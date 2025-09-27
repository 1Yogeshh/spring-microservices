package com.example.product_service.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    // Manual Base64-encoded 256-bit key
    private final SecretKey SECRET_KEY;

    public JwtUtil() {
        // ⚠️ Make sure your key is at least 32 bytes (256 bits)
        String keyString = "rjX1v3x8ePq2j5L0qD5Zk5W2j7lK8eF3xM2nP1aJ4hY=";
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        this.SECRET_KEY = Keys.hmacShaKeyFor(decodedKey);
    }

    // Extract username from token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
