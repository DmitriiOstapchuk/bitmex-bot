package com.dmitrii.ostapchuk.bitmexBot.service.impl;


import com.dmitrii.ostapchuk.bitmexBot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserAuthServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserAuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("USER"))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User was not found"));
    }
}
