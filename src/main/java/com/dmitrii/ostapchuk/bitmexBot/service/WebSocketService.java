package com.dmitrii.ostapchuk.bitmexBot.service;

import com.dmitrii.ostapchuk.bitmexBot.model.WebSocketMessage;
import jakarta.websocket.*;

import java.io.IOException;
import java.time.Instant;

public interface WebSocketService {
    void connect(String subscription);
    @OnMessage
    void onMessage(String message);
    @OnOpen
    void onOpen ();
    @OnMessage
    void onMessage(PongMessage pongMessage);
    @OnClose
    void onClose();
    @OnError
    void onError(Throwable error);
    void sendWebSocketMessage (String opValue, Object[] argsValue);
    void setFibonacciAlgorithmService(FibonacciAlgorithmService fibonacciAlgorithmService);
}
