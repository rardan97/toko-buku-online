package com.blackcode.user.service;

import com.blackcode.common.dto.user.UserTokenRes;

public interface UserTokenService {

    void processUserTokenAdd(Long userId, String jwt);



    void processUserTokenRefresh(String email, String jwt);

    UserTokenRes findByUserId(Long userId, String token);

}
