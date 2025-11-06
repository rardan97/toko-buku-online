package com.blackcode.user.service.impl;

import com.blackcode.common.dto.user.*;
import com.blackcode.common.exception.EmailAlreadyExistsException;
import com.blackcode.user.model.Role;
import com.blackcode.user.model.UserRefreshToken;
import com.blackcode.user.model.Users;
import com.blackcode.user.repository.UserTokenRepository;
import com.blackcode.user.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserServiceImpl implements AuthUserService {

    private static final Logger logger = LoggerFactory.getLogger(AuthUserServiceImpl.class);

    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final UserTokenRepository userTokenRepository;

    private final UserTokenService userTokenService;

    private final JwtUtilsService jwtUtilsService;

    private final UserRefreshTokenService userRefreshTokenService;

    public AuthUserServiceImpl(PasswordEncoder encoder, AuthenticationManager authenticationManager, UserService userService, UserTokenRepository userTokenRepository, UserTokenService userTokenService, JwtUtilsService jwtUtilsService, UserRefreshTokenService userRefreshTokenService) {
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userTokenRepository = userTokenRepository;
        this.userTokenService = userTokenService;
        this.jwtUtilsService = jwtUtilsService;
        this.userRefreshTokenService = userRefreshTokenService;
    }

    @Override
    public UserRegisterRes userRegister(UserRegisterReq userRegisterReq) {
        if (userService.existsByEmail(userRegisterReq.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email already registered: " + userRegisterReq.getEmail()
            );
        }

        Users users = new Users();
        users.setName(userRegisterReq.getName());
        users.setEmail(userRegisterReq.getEmail());
        users.setRole(Role.valueOf(userRegisterReq.getRole().toUpperCase()));
        users.setPassword(encoder.encode(userRegisterReq.getPassword()));
        UserRes savedUser = userService.saveUser(users);

        System.out.println("Email: "+savedUser.getEmail());

        return new UserRegisterRes(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreated_at());
    }

    @Override
    public UserLoginRes userLogin(UserLoginReq userLoginReq) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginReq.getUsername(), userLoginReq.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtilsService.generateTokenFromEmail(userDetails.getUsername());
        userTokenService.processUserTokenAdd(userDetails.getId(), jwt);
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(item -> item.getAuthority())
                .orElse("ROLE_DEFAULT");

        UserRefreshToken refreshToken = userRefreshTokenService.createRefreshToken(jwt, userDetails.getId());

        logger.info("User {} signed in successfully", userDetails.getUsername());

        return new UserLoginRes(
                jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                role
        );
    }

    @Override
    public TokenRefreshRes refreshToken(TokenRefreshReq tokenRefreshReq) {
        String requestRefreshToken = tokenRefreshReq.getRefreshToken();
        UserRefreshToken refreshToken = userRefreshTokenService.findByToken(requestRefreshToken);
        refreshToken = userRefreshTokenService.verifyExpiration(refreshToken);
        Users user = refreshToken.getUser();
        String newToken = jwtUtilsService.generateTokenFromEmail(user.getEmail());
        userTokenService.processUserTokenRefresh(user.getEmail(), newToken);
        return new TokenRefreshRes(newToken, requestRefreshToken);
    }
}
