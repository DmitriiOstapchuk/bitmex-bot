package com.dmitrii.ostapchuk.bitmexBot.service.impl;


import com.dmitrii.ostapchuk.bitmexBot.model.WebSocketMessage;
import com.dmitrii.ostapchuk.bitmexBot.service.FibonacciAlgorithmService;
import com.dmitrii.ostapchuk.bitmexBot.service.WebSocketService;
import com.dmitrii.ostapchuk.bitmexBot.util.PingTimer;
import com.dmitrii.ostapchuk.bitmexBot.util.generator.SignatureGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.tyrus.spi.ClientContainer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@ClientEndpoint
@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {
    private FibonacciAlgorithmService fibonacciAlgorithmService;
    private final ObjectMapper objectMapper;
    private final SignatureGenerator signatureGenerator;
    private WebSocketContainer container;
    private final String SERVER_URI = "wss://ws.testnet.bitmex.com/realtime";
    private Session session;
    private Boolean isConnected;
    private AtomicLong lastMessageTime = new AtomicLong();
    private final int MAX_MSG_DELAY = 60;
    private PingTimer pingTimer;
    private ScheduledExecutorService orderValidatorTimer = null;
    private final int RECONNECT_SECONDS = 5;
    private final int EXPIRES_DELAY = 5;

    private void stopWebsocketThreads() {
        try {
            if (container != null && container instanceof ClientContainer clientContainer) {
                container = null;
                log.debug("Container is stopped");
            }
            if (session != null && session.isOpen()) {
                session.close();
                log.debug("Session is closed");
            }
        } catch (Exception e) {
            log.error("Error during cleanup websocket threads: " + e.getMessage());
        }
    }

    private void launchPingTimer() {
        if (pingTimer == null) {
            log.info("Started Ping timer");
            pingTimer = new PingTimer(session);
            ScheduledExecutorService scheduler = pingTimer.getScheduler();
            scheduler.scheduleWithFixedDelay(() -> {
                long l = lastMessageTime.get();
                if (System.currentTimeMillis() - l < MAX_MSG_DELAY + 2000) {
                    sendPing();
                } else if(isConnected) {
                   pingTimer.startPingTimer();
                }
            }, 0, MAX_MSG_DELAY, TimeUnit.MILLISECONDS);
        }
    }

    private void sendPing() {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendPing(ByteBuffer.wrap(new byte[0]));
            }
        } catch (IOException e) {
            log.error("Error on sending ping: ", e);
        }
    }

    public void connect(String subscription) {
        try {
            container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, new URI(SERVER_URI));
            session.setMaxIdleTimeout(TimeUnit.MINUTES.toMillis(MAX_MSG_DELAY));
            if (session.isOpen()) {
                isConnected = true;
                log.info("Session is opened");
                long expires = System.currentTimeMillis() / 1000 + EXPIRES_DELAY;
                String apiKey = fibonacciAlgorithmService.getRestOrderService().getApiKey();
                String apiSecretKey = fibonacciAlgorithmService.getRestOrderService().getApiSecretKey();
                String signatureStr = signatureGenerator.getSignature(apiSecretKey, "GET/realtime" + expires);
                sendWebSocketMessage("authKeyExpires", new Object[]{apiKey, expires, signatureStr});
                sendWebSocketMessage("subscribe", new Object[]{subscription});
            }

         launchPingTimer();

        } catch (URISyntaxException | IOException | DeploymentException e) {
            log.error("Cannot connect to the simulator server.");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @OnMessage
    public void onMessage(String message) {
        log.info(message);
        lastMessageTime.set(System.currentTimeMillis());
        fibonacciAlgorithmService.handleMessage(message);
    }
    @OnOpen
    public void onOpen () {
    }

    @OnMessage
    public void onMessage(PongMessage pongMessage) {
        long curTime = System.currentTimeMillis();
        log.debug("Received pong message UTC=" + Instant.ofEpochMilli(curTime));
        lastMessageTime.set(curTime);
    }

    @OnClose
    public void onClose() {
    }

    @OnError
    public void onError(Throwable error) {
        log.info("error: " + error.getMessage());
    }

    public void sendWebSocketMessage (String opValue, Object[] argsValue) {
        WebSocketMessage message = WebSocketMessage.builder()
                                .op(opValue)
                                .args(argsValue)
                                .build();
        try {
            String text = objectMapper.writeValueAsString(message);
            log.info("Message for subscription is " + text);
            RemoteEndpoint.Basic basicRemote = session.getBasicRemote();
            basicRemote.sendText(text);
            log.info("The message was sent for configuring websocket client: " + text);
        } catch(IOException e) {
            log.warn("Something went wrong with sending message for websocket configuring");
        }
    }
}
