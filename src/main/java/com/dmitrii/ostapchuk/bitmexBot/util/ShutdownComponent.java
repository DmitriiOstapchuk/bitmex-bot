package com.dmitrii.ostapchuk.bitmexBot.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShutdownComponent {
    private final ApplicationContext applicationContext;

    public void shutdown() {
        int exitCode = SpringApplication.exit(applicationContext);
        System.exit(exitCode);
    }

}
