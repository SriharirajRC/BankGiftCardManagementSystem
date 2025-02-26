package com.Utils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class Jwtutils {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String ENCODED_SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getEncoded());
    private static final long EXPIRATION_TIME = 60 * 60 * 1000; // 1-hour expiration

    public static String generateToken(String userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Key getKey() {
        byte[] keyBytes = Base64.getDecoder().decode(ENCODED_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token);
        return (claims != null) ? claimResolver.apply(claims) : null;
    }

    public static Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.err.println("Invalid Token: " + e.getMessage());
            return null;
        }
    }

    public static boolean validateToken(String token) {
        return token != null && !isTokenExpired(token);
    }

    private static boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration != null && expiration.before(new Date());
    }

    private static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
    
}
