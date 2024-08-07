package com.dmitrii.ostapchuk.bitmexBot.security;


import com.dmitrii.ostapchuk.bitmexBot.service.impl.UserAuthServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserAuthServiceImpl userAuthServiceImpl;

    @Bean
    public MyAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new MyAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException{
                response.sendRedirect("/api");
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authz -> authz
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/api/register").permitAll()
                        .anyRequest().authenticated())
                .formLogin(withDefaults -> withDefaults
                        .loginPage("/api/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .successHandler(authenticationSuccessHandler())
                )
                .logout(withDefaults -> withDefaults
                        .logoutSuccessUrl("/api/login")
                        .permitAll());
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userAuthServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }
    public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException{
            response.sendRedirect("/api");
        }
    }
}
