package com.ex.project1.security;



//package com.ex.project1.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey key =
            Keys.hmacShaKeyFor(
                "mysecretkeymysecretkeymysecretkey123456"
                .getBytes()
            );

    public String generateToken(String accountnumber)
    {
        return Jwts.builder()
                .subject(accountnumber)
                .issuedAt(new Date())
                .expiration(
                    new Date(System.currentTimeMillis() + 86400000)
                )
                .signWith(key)
                .compact();
    }

    public String getAccountnumber(String token)
    {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token)
    {
        try
        {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
