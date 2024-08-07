package com.dmitrii.ostapchuk.bitmexBot.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WebSocketMessage {
    private String op;
    private Object[] args;
}
