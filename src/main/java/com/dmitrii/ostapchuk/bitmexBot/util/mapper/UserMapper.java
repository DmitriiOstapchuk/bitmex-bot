package com.dmitrii.ostapchuk.bitmexBot.util.mapper;

import com.dmitrii.ostapchuk.bitmexBot.model.dto.UserTO;
import com.dmitrii.ostapchuk.bitmexBot.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    public User toEntity (UserTO userTO) {
        User user = new User();
        user.setUsername(userTO.getUsername());
        user.setPassword(passwordEncoder.encode(userTO.getPassword()));
        user.setKey(userTO.getKey());
        user.setSecretKey(userTO.getSecretKey());
        return user;
    }

}
