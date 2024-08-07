package com.dmitrii.ostapchuk.bitmexBot.repository;

import com.dmitrii.ostapchuk.bitmexBot.model.entity.Order;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Data
public class OrderRepository {
    private Map<String, Order> openBuyOrders = new HashMap<>();
    private Map<String, Order> openSellOrders = new HashMap<>();
    private String additionalBuyOrderId;
}
