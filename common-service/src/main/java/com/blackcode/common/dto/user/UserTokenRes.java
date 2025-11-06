package com.blackcode.common.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserTokenRes {

    private Long id;

    private UserRes user;

    private String token;

    private Boolean isActive = true;

    private LocalDateTime expiryDate;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
