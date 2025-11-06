package com.blackcode.common.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginReq {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
