package com.blackcode.web.controller;

import com.blackcode.common.dto.user.*;
import com.blackcode.common.utils.ApiResponse;
import com.blackcode.user.service.AuthUserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthUserService authUserService;

    public AuthController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserRegisterRes>> userRegister(@Valid @RequestBody UserRegisterReq userRegisterReq) {
        UserRegisterRes userRegisterRes = authUserService.userRegister(userRegisterReq);
        return ResponseEntity.ok(ApiResponse.success("Login Success", 200, userRegisterRes));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginRes>> userLogin(@Valid @RequestBody UserLoginReq userLoginReq) {
        UserLoginRes userLoginRes = authUserService.userLogin(userLoginReq);
        return ResponseEntity.ok(ApiResponse.success("Login Success", 200, userLoginRes));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse<TokenRefreshRes>> refreshToken(@Valid @RequestBody TokenRefreshReq tokenRefreshReq) {
        TokenRefreshRes tokenRefreshRes = authUserService.refreshToken(tokenRefreshReq);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", 200, tokenRefreshRes));
    }
}
