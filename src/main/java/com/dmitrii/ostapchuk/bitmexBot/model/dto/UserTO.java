package com.dmitrii.ostapchuk.bitmexBot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTO {
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Repeated password cannot be blank")
    private String repeatPassword;

    @NotBlank(message = "Key cannot be blank")
    private String key;

    @NotBlank(message = "secretKey cannot be blank")
    private String secretKey;
}
