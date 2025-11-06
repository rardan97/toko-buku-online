package com.blackcode.user.service.impl;

import com.blackcode.common.dto.user.UserRes;
import com.blackcode.common.exception.DataNotFoundException;
import com.blackcode.user.model.Users;
import com.blackcode.user.repository.UserRepository;
import com.blackcode.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserRes getUserById(Long userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found with id : "+userId));
        return mapToUserRes(users);
    }

    @Override
    public UserRes findByEmail(String email) {
        Users users = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found with email : "+email));

        return mapToUserRes(users);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserRes saveUser(Users users) {
        Users usersData = userRepository.save(users);
        return mapToUserRes(usersData);
    }

    private UserRes mapToUserRes(Users users) {
        UserRes userRes = new UserRes();
        userRes.setId(users.getId());
        userRes.setName(users.getName());
        userRes.setEmail(users.getEmail());
        userRes.setRole(users.getRole().name());
        userRes.setCreated_at(users.getCreated_at());
        return userRes;
    }


}
