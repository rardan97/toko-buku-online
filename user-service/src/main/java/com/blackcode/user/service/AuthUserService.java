package com.blackcode.user.service;

import com.blackcode.common.dto.user.*;
import org.springframework.stereotype.Service;

@Service
public interface AuthUserService {

    UserRegisterRes userRegister(UserRegisterReq userRegisterReq);

    UserLoginRes userLogin(UserLoginReq userLoginReq);

    TokenRefreshRes refreshToken(TokenRefreshReq tokenRefreshReq);


}