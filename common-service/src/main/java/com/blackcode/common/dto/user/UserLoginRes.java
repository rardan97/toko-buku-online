package com.blackcode.common.dto.user;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginRes {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long userId;
    private String username;
    private String role;

    public UserLoginRes(String token, String refreshToken, Long userId, String username, String role) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }


}
