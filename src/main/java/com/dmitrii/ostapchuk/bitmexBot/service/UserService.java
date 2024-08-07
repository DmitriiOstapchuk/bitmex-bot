package com.dmitrii.ostapchuk.bitmexBot.service;

import com.dmitrii.ostapchuk.bitmexBot.model.dto.UserTO;
import com.dmitrii.ostapchuk.bitmexBot.model.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    @Transactional
    void create (UserTO userTO);
    User findByUsername (String username);
}
