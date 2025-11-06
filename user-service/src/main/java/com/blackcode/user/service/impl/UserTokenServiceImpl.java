package com.blackcode.user.service.impl;


import com.blackcode.common.dto.user.UserRes;
import com.blackcode.common.dto.user.UserTokenRes;
import com.blackcode.common.exception.DataNotFoundException;
import com.blackcode.user.model.UserToken;
import com.blackcode.user.model.Users;
import com.blackcode.user.repository.UserRepository;
import com.blackcode.user.repository.UserTokenRepository;
import com.blackcode.user.service.UserTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserTokenServiceImpl implements UserTokenService {

    @Value("${blackcode.app.jwtRefreshExpirationMs}")
    private int refreshTokenDurationMs;

    @Value("${blackcode.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private final UserTokenRepository userTokenRepository;

    private final UserRepository userRepository;

    public UserTokenServiceImpl(UserTokenRepository userTokenRepository, UserRepository userRepository) {
        this.userTokenRepository = userTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void processUserTokenAdd(Long userId, String jwt) {
        Date date = new Date((new Date()).getTime() + jwtExpirationMs);
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Optional<UserToken> userTokenData = userTokenRepository.findByUserIdNative(userId);
        if(userTokenData.isPresent()){
            userTokenData.get().setToken(jwt);
            userTokenData.get().setIsActive(true);
            userTokenData.get().setExpiryDate(localDateTime);
            userTokenRepository.save(userTokenData.get());
        }else{

            Users dataUser = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found with userId : "+userId));
            UserToken userToken = new UserToken();
            userToken.setToken(jwt);
            userToken.setUser(dataUser);
            userToken.setIsActive(true);
            userToken.setExpiryDate(localDateTime);
            userTokenRepository.save(userToken);
        }
    }

    @Override
    public void processUserTokenRefresh(String email, String jwt) {
        Date date = new Date((new Date()).getTime() + jwtExpirationMs);
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Users dataUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found with email : "+email));

        UserToken userTokenData = userTokenRepository.findByUserIdNative(dataUser.getId())
                .orElseThrow(() -> new DataNotFoundException("UserToken not found with id : "+dataUser.getId()));

        userTokenData.setToken(jwt);
        userTokenData.setIsActive(true);
        userTokenData.setExpiryDate(localDateTime);
        userTokenRepository.save(userTokenData);
    }

    @Override
    public UserTokenRes findByUserId(Long userId, String token) {
        UserToken userToken = userTokenRepository.findByUserIdAndTokenNative(userId, token)
                .orElseThrow(() -> new DataNotFoundException("UserToken not found with userId : "+userId));

        System.out.println("======Check token : "+userToken.getToken());

        return mapToUserTokenRes(userToken);
    }

    private UserTokenRes mapToUserTokenRes(UserToken userToken) {
        UserTokenRes userTokenRes = new UserTokenRes();
        userTokenRes.setId(userToken.getId());
        userTokenRes.setToken(userToken.getToken());
        userTokenRes.setUser(mapToUserRes(userToken.getUser()));
        userTokenRes.setIsActive(userToken.getIsActive());
        userTokenRes.setExpiryDate(userToken.getExpiryDate());
        return userTokenRes;
    }

    private UserRes mapToUserRes(Users users) {
        UserRes userRes = new UserRes();
        userRes.setId(users.getId());
        userRes.setName(users.getName());
        userRes.setRole(users.getRole().name());
        userRes.setCreated_at(users.getCreated_at());
        return userRes;
    }
}
