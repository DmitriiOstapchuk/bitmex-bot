package com.dmitrii.ostapchuk.bitmexBot.service.impl;

import com.dmitrii.ostapchuk.bitmexBot.model.entity.Order;
import com.dmitrii.ostapchuk.bitmexBot.model.OrderSide;
import com.dmitrii.ostapchuk.bitmexBot.repository.OrderRepository;
import com.dmitrii.ostapchuk.bitmexBot.service.DatabaseOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class DatabaseOrderServiceImpl implements DatabaseOrderService {
    private OrderRepository orderRepository;
    public String getAdditionalBuyOrderId() {
        return orderRepository.getAdditionalBuyOrderId();
    }
    public void setAdditionalBuyOrderId(String orderId) {
        orderRepository.setAdditionalBuyOrderId(orderId);
    }
    public Order getAdditionalBuyOrder(String additionalOrderId) {
        return orderRepository.getOpenBuyOrders().get(additionalOrderId);
    }

    public boolean checkIfPresentInDatabaseBuy(Order order) {
        return orderRepository.getOpenBuyOrders().containsKey(order.getOrderId());
    }

    @Override
    public boolean checkIfPresentInDatabaseSell(Order order) {
        return orderRepository.getOpenSellOrders().containsKey(order.getOrderId());
    }

    public Order[] getOpenBuyOrders () {
        Order[] orders = orderRepository.getOpenBuyOrders().values().toArray(new Order[0]);
        return orders;
    }
    public Order getOpenBuyOrder(String orderId) {
        return orderRepository.getOpenBuyOrders().get(orderId);
    }
    public Order[] getOpenSellOrders () {
        Order[] orders = orderRepository.getOpenSellOrders().values().toArray(new Order[0]);
        return orders;
    }
    public Order getOpenSellOrder(String orderId) {
        return orderRepository.getOpenSellOrders().get(orderId);
    }
    public void addOrder (Order order) {
        boolean isBuy = order.getSide().equals(OrderSide.Buy);
        if (isBuy) {
            addBuyOrder(order);
            log.info("now orderRepository has " + getOpenBuyOrders().length + " orders");
        } else {
            addSellOrder(order);
        }
    }
    public void addBuyOrder (Order order) {
        String orderId = order.getOrderId();
        orderRepository.getOpenBuyOrders().put(orderId, order);
        log.info("Order " + order.getOrderId() + " was added to \"OpenBuyOrders\" HashMap");
    }
    public void addSellOrder (Order order) {
        String orderId = order.getOrderId();
        orderRepository.getOpenSellOrders().put(orderId, order);
        log.info("Order " + order.getOrderId() + " was added to \"OpenSellOrders\" HashMap");
    }
    public void deleteOrder(Order order) {
        boolean isBuy = orderRepository.getOpenBuyOrders().containsKey(order.getOrderId());
        if (isBuy) {
            deleteBuyOrder(order);
        } else {
            deleteSellOrder(order);
        }
    }
    public void deleteSellOrder(Order order) {
        orderRepository.getOpenSellOrders().remove(order.getOrderId());
        log.info("Order " + order.getOrderId() + " was deleted from \"OpenSellOrders\" HashMap");
    }
    public void deleteBuyOrder(Order order) {
        orderRepository.getOpenBuyOrders().remove(order.getOrderId());
        log.info("Order " + order.getOrderId() + " was deleted from \"OpenBuyOrders\" HashMap");
    }
    public void clearBuyOrders() {
        orderRepository.getOpenBuyOrders().clear();
        log.info("All orders were deleted from \"OpenBuyOrders\" HashMap");
    }
    public void clearSellOrders() {
        orderRepository.getOpenSellOrders().clear();
        log.info("All orders were deleted from \"OpenSellOrders\" HashMap");
    }

}
