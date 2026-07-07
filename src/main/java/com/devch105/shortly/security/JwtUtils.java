package com.devch105.shortly.security;


import com.devch105.shortly.service.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh_expiration}")
    private Long refreshExpiration;

    private static final String ROLES = "roles";
    private SecretKey key ;

    @PostConstruct
    private void init(){
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateToken(UserDetailsImpl userDetails) {
        String email = userDetails.getEmail();
        String roles = userDetails.getAuthorities().stream().map(
                authority -> authority.getAuthority())
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(email)
                .claim(ROLES,roles)
                .issuedAt(new Date())
                .expiration(new Date( System.currentTimeMillis()+ expiration))
                .signWith(key)
                .compact();
    }


    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build().parseSignedClaims(token).getPayload();
    }

    public String getUserNameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (JwtException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
