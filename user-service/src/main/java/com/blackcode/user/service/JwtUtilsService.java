package com.blackcode.user.service;

public interface JwtUtilsService {

    String generateTokenFromEmail(String username);

    String getUserNameFromJwtToken(String token);

    void assertValidToken(String token);
}
