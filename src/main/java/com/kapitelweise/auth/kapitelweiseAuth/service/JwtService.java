package com.kapitelweise.auth.kapitelweiseAuth.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;

// hier generieren wir den Token, der dann an den User geschickt wird. Wir können da unterschiedliche Sachen mitgeben, hab jetzt einfach mal das


@Service
public class JwtService {

    private final String secretKey;

    public JwtService(@Value("${app.jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }
    
public String generateToken(String username){
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new java.util.Date())
        .setExpiration(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
        .addClaims(new java.util.HashMap<>())
        .signWith(getSignedKey(), SignatureAlgorithm.HS256)
        .compact();


}

    private Key getSignedKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

}




