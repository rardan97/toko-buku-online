package com.blackcode.common.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegisterRes {
    private Long id;
    private String name;
    private String email;
    private String role;
    private Timestamp created_at;
}
