package com.dmitrii.ostapchuk.bitmexBot.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column (name = "username", unique = true, nullable = false)
    private String username;
    @Column (nullable = false)
    private String password;
    @Column (nullable = false)
    private String key;
    @Column (name = "secret_key", nullable = false)
    private String secretKey;
}
