package com.example.oauth2.global.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

public class JwtProvider {

    public static String secretKey="secret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_keysecret_key";

    public static String generateToken(String oauth2Id, int validTime) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(JwtProvider.secretKey.getBytes(StandardCharsets.UTF_8));
            return Jwts.builder()
                    .setHeader(Map.of("typ", "JWT"))
                    .setSubject(oauth2Id)
                    .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                    .setExpiration(
                            Date.from(ZonedDateTime.now().plusMinutes(validTime).toInstant()))
                    .signWith(key)
                    .compact();
        } catch (Exception e) {
            throw new JwtException(e.getMessage());
        }
    }

    public static String validateToken(String token) {
        try {
            SecretKey key =
                    Keys.hmacShaKeyFor(JwtProvider.secretKey.getBytes(StandardCharsets.UTF_8));

            String subject = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();

            return subject;
        } catch (ExpiredJwtException expiredJwtException) {
            throw new JwtException("expired token");
        } catch (Exception e) {
            throw new JwtException(e.getMessage());
        }
    }
}
