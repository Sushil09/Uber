package com.sjsushil09.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTokenProvider {
    private final long validityInMilliseconds = 30000;
    private final String secretKey = "@#%R@C#%QCQ#$%V#$VT$%W^$%^$%^W$%^";

    public String createToken(CustomUserDetails user) {
        // will take a user account, and return a JWT token
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("auth", user.getAuthorities());

        Date expirationDate = new Date(new Date().getTime() + validityInMilliseconds);

        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return jwtToken;
    }

    public CustomUserDetails parseToken(String token) {
        return null;
        // going to take a token

        // return a valid userdetails
    }
}
