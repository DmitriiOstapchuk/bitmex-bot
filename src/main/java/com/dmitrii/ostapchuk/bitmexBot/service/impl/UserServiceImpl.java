package com.dmitrii.ostapchuk.bitmexBot.service.impl;

import com.dmitrii.ostapchuk.bitmexBot.model.dto.UserTO;
import com.dmitrii.ostapchuk.bitmexBot.model.entity.User;
import com.dmitrii.ostapchuk.bitmexBot.repository.UserRepository;
import com.dmitrii.ostapchuk.bitmexBot.service.UserService;
import com.dmitrii.ostapchuk.bitmexBot.util.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public void create(UserTO userTO) {
        userRepository.save(userMapper.toEntity(userTO));
        log.info("New user with username " + userTO.getUsername() + " was added to database");
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }
}
