package com.dmitrii.ostapchuk.bitmexBot.util;

import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PingTimer {
    private final Session session;
    private final ScheduledExecutorService scheduler;

    public PingTimer(Session session) {
        this.session = session;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void startPingTimer() {
        scheduler.scheduleAtFixedRate(this::sendPing, 0, 30, TimeUnit.SECONDS);
    }

    private void sendPing() {
        try {
            session.getBasicRemote().sendPing(null);
        } catch (IOException e) {
            log.warn("There are some problems with ping sending");
        }
    }

    public void stopPingTimer() {
        scheduler.shutdown();
    }

    public ScheduledExecutorService getScheduler () {
        return scheduler;
    }
}
