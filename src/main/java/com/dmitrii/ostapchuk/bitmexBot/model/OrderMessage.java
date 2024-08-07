package com.dmitrii.ostapchuk.bitmexBot.model;

import com.dmitrii.ostapchuk.bitmexBot.model.entity.Order;
import lombok.Data;

@Data
public class OrderMessage {
    private String table;
    private String action;
    private Order[] data;
}
