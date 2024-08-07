package com.dmitrii.ostapchuk.bitmexBot.repository;

import com.dmitrii.ostapchuk.bitmexBot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
