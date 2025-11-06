package com.blackcode.user.service;

import com.blackcode.common.dto.user.UserRes;
import com.blackcode.user.model.Users;

public interface UserService {

    UserRes getUserById(Long userId);

    UserRes findByEmail(String email);

    boolean existsByEmail(String email);

    UserRes saveUser(Users users);

}
