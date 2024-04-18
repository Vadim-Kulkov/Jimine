package com.jimine.jiminebackend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

//    @Value("${app.jwt-secret}")
    private String jwtSecret = "jimine";
//    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationDate = 240_000;

    public String generateToken(Authentication authentication){

        String username = authentication.getName();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();

        return token;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsername(String token){

//        return Jwts.parser()
//                .verifyWith((SecretKey) key())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .getSubject();
        String subject = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
        return subject;
    }

    public boolean validateToken(String token){
        Jwts.parserBuilder()
                .setSigningKey(key())
                .build().parseClaimsJws(token);
        return true;

    }
}