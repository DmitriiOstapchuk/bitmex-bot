package com.dmitrii.ostapchuk.bitmexBot.service;

import com.dmitrii.ostapchuk.bitmexBot.model.Symbol;
import com.dmitrii.ostapchuk.bitmexBot.model.dto.OrderTO;
import com.dmitrii.ostapchuk.bitmexBot.model.entity.Order;

public interface RestOrderService {
    Double getActualPrice();
    Order[] sendOrder(OrderTO orderTO);
    void cancelOrders(Order[] orders);
    void getAllOrders();
    boolean getOpenOrders(Symbol symbol);
    void setApiKey(String apiKey);
    void setApiSecretKey(String secretKey);
    void setSymbol (Symbol symbol);
    void setBaseUrl (String baseUrl);
    Symbol getSymbol();
    String getApiKey();
    String getApiSecretKey();
}
