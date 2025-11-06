package com.blackcode.user.service.impl;

import com.blackcode.common.exception.InvalidJwtException;
import com.blackcode.common.exception.TokenExpiredException;
import com.blackcode.user.service.JwtUtilsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Service
public class JwtUtilsServiceImpl implements JwtUtilsService {

    @Value("${blackcode.app.jwtSecret}")
    private String jwtSecret;

    @Value("${blackcode.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Override
    public String generateTokenFromEmail(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public void assertValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

        }catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Token has expired", e);
        } catch (JwtException e) {
            throw new InvalidJwtException("Invalid JWT token", e);
        } catch (IllegalArgumentException e) {
            throw new InvalidJwtException("Token is null or empty", e);
        }
    }

    private Key getSigningKey(){
        return new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }
}
