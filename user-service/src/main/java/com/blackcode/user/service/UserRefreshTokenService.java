package com.blackcode.user.service;

import com.blackcode.user.model.UserRefreshToken;

import java.util.Optional;

public interface UserRefreshTokenService {

    UserRefreshToken findByToken(String token);

    UserRefreshToken createRefreshToken(String jwt, Long userId);

    UserRefreshToken verifyExpiration(UserRefreshToken token);

    void deleteByUserId(Long userId);
}