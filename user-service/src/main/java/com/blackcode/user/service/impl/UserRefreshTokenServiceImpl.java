package com.blackcode.user.service.impl;

import com.blackcode.common.exception.DataNotFoundException;
import com.blackcode.common.exception.TokenRefreshException;
import com.blackcode.user.model.UserRefreshToken;
import com.blackcode.user.model.Users;
import com.blackcode.user.repository.UserRefreshTokenRepository;
import com.blackcode.user.repository.UserRepository;
import com.blackcode.user.service.UserRefreshTokenService;
import com.blackcode.user.service.UserTokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserRefreshTokenServiceImpl implements UserRefreshTokenService {

    @Value("${blackcode.app.jwtRefreshExpirationMs}")
    private int refreshTokenDurationMs;

    private final UserRefreshTokenRepository userRefreshTokenRepository;

    private final UserRepository userRepository;

    private final UserTokenService userTokenService;

    public UserRefreshTokenServiceImpl(UserRefreshTokenRepository userRefreshTokenRepository, UserRepository userRepository, UserTokenService userTokenService) {
        this.userRefreshTokenRepository = userRefreshTokenRepository;
        this.userRepository = userRepository;
        this.userTokenService = userTokenService;
    }

    @Override
    public UserRefreshToken findByToken(String token) {
        return userRefreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new DataNotFoundException("UserRefreshToken not found with token : "+token));
    }

    @Override
    public UserRefreshToken createRefreshToken(String jwt, Long userId) {
        UserRefreshToken userRefreshToken = null;
        Optional<UserRefreshToken> existingToken = userRefreshTokenRepository.findByUserId(userId);
        if (existingToken.isPresent()) {
            userRefreshToken = new UserRefreshToken();
            userRefreshToken.setId(existingToken.get().getId());
            userRefreshToken.setUser(existingToken.get().getUser());
            userRefreshToken.setExpiryDate(existingToken.get().getExpiryDate());
            userRefreshToken.setToken(existingToken.get().getToken());
        }else{
            Users dataUser = userRepository.findById(userId).get();
            userRefreshToken = new UserRefreshToken();
            userRefreshToken.setUser(dataUser);
            userRefreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            userRefreshToken.setToken(UUID.randomUUID().toString());
            userRefreshToken = userRefreshTokenRepository.save(userRefreshToken);
        }
        userTokenService.processUserTokenAdd(userId, jwt);
        return userRefreshToken;
    }

    @Override
    public UserRefreshToken verifyExpiration(UserRefreshToken token) {
        if(token.getExpiryDate().compareTo(Instant.now()) < 0){
            userRefreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        userRefreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
