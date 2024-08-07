package com.dmitrii.ostapchuk.bitmexBot.service;

import com.dmitrii.ostapchuk.bitmexBot.model.entity.Order;

public interface DatabaseOrderService {
    String getAdditionalBuyOrderId();
    void setAdditionalBuyOrderId(String orderId);
    Order getAdditionalBuyOrder(String orderId);
    boolean checkIfPresentInDatabaseBuy(Order order);
    boolean checkIfPresentInDatabaseSell(Order order);
    Order[] getOpenBuyOrders();
    Order getOpenBuyOrder(String orderId);
    Order[] getOpenSellOrders();
    Order getOpenSellOrder(String orderId);
    void addOrder(Order order);
    void addBuyOrder (Order order);
    void addSellOrder (Order order);
    void deleteOrder(Order order);
    void deleteSellOrder(Order order);
    void deleteBuyOrder(Order order);
    void clearBuyOrders();
    void clearSellOrders();

}
