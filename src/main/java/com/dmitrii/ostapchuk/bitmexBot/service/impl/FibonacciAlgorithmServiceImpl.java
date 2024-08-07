package com.dmitrii.ostapchuk.bitmexBot.service.impl;


import com.dmitrii.ostapchuk.bitmexBot.model.OrderSide;
import com.dmitrii.ostapchuk.bitmexBot.model.OrderStatus;
import com.dmitrii.ostapchuk.bitmexBot.model.OrderType;
import com.dmitrii.ostapchuk.bitmexBot.model.dto.OrderTO;
import com.dmitrii.ostapchuk.bitmexBot.model.entity.Order;
import com.dmitrii.ostapchuk.bitmexBot.service.FibonacciAlgorithmService;
import com.dmitrii.ostapchuk.bitmexBot.service.DatabaseOrderService;
import com.dmitrii.ostapchuk.bitmexBot.service.RestOrderService;
import com.dmitrii.ostapchuk.bitmexBot.util.ShutdownComponent;
import com.dmitrii.ostapchuk.bitmexBot.util.handler.MessageMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class FibonacciAlgorithmServiceImpl implements FibonacciAlgorithmService, DisposableBean {
    private final RestOrderService restOrderService;
    private final DatabaseOrderService databaseOrderService;
    private final MessageMapper messageMapper;
    private final ShutdownComponent shutdownComponent;
    private final int[] fibonacciLevels = new int[] {1, 1, 1, 2, 3, 5, 8, 13, 21, 34};
    private int numberOfLevels;
    private int step;
    private int initialQty;
    private int numberOfCycles;
    private boolean isDemo;
    private int currentCycle = 1;
    private int miniLvl;
    private double initialPrice;
    private double lastBuyPrice = 0;
    private double additionalBuyOrderQty = 0;


    public void startNewCycle() {
        double actualPrice = restOrderService.getActualPrice();
        sendInitialBuyOrders(actualPrice);
        log.info("New algorithm cycle was started");
    }


    public void handleMessage(String message) {
        try {
            Order[] orders = messageMapper.getOrderInfosFromMessage(message);
            for (Order order : orders) {
                boolean statusCondition = isDemo
                        ? order.getStatus().equals(OrderStatus.Canceled)
                        : order.getStatus().equals(OrderStatus.Filled);
                if (order.getSide().equals(OrderSide.Buy) && statusCondition) {
                    if (databaseOrderService.checkIfPresentInDatabaseBuy(order)) {
                        log.info("Sending new Sell orders");
                        Order orderFromDB = databaseOrderService.getOpenBuyOrder(order.getOrderId());
                        deleteOrdersFromDatabase(orderFromDB);
                        lastBuyPrice = orderFromDB.getPrice();
                        boolean isAdditional = false;
                        if (order.getOrderId().equals(databaseOrderService.getAdditionalBuyOrderId())) {
                            databaseOrderService.setAdditionalBuyOrderId(null);
                            isAdditional = true;
                        }
                        sendSellOrders(orderFromDB.getPrice(), orderFromDB.getLevelInOrderDB(), isAdditional);
                    } else {
                        log.info("This buy order isn't operated by algorithm service");
                    }
                } else if (order.getSide().equals(OrderSide.Sell) && statusCondition) {
                    if (databaseOrderService.getOpenSellOrders().length == 1) {
                        cancelAllBuyOrders();
                        cancelAllSellOrders();
                        if (numberOfCycles == Integer.MAX_VALUE) {
                            log.info("The algorithm cycle was completed");
                        } else {
                            int cyclesRemaining = numberOfCycles - currentCycle;
                            log.info("The algorithm cycle was completed. The number of cycles remaining: " + cyclesRemaining);
                        }
                        if (currentCycle >= numberOfCycles) {
                            log.info("Algorithm service was stopped");
                            shutdownComponent.shutdown();
                        } else {
                            startNewCycle();
                            currentCycle++;
                        }
                    } else {
                        Order orderFromDB = databaseOrderService.getOpenSellOrder(order.getOrderId());
                        deleteOrdersFromDatabase(orderFromDB);
                        sendAdditionalBuyOrder(orderFromDB.getLevelInOrderDB());
                    }
                }
            }
        } catch (Exception e ) {
            log.warn("This message wasn't handled because it doesn't consist order info");
        }

    }


    private void sendInitialBuyOrders (double actualPrice) {
        log.info("Sending initial buy orders");
        this.setInitialPrice(actualPrice);
        databaseOrderService.setAdditionalBuyOrderId(null);
        for (int i = 0; i < numberOfLevels; i++) {
            OrderTO orderTO = OrderTO.builder()
                    .symbol(restOrderService.getSymbol())
                    .side(OrderSide.Buy)
                    .orderQty(initialQty * fibonacciLevels[i])
                    .price(actualPrice = actualPrice-(step * fibonacciLevels[i]))
                    .orderType(OrderType.Limit)
                    .build();
            Order order = restOrderService.sendOrder(orderTO)[0];
            order.setLevelInOrderDB(i + 1);
            addOrderToDatabase(order);
        }
    }

    private void sendSellOrders (double lastBuyPrice, int levelOfFilledBuyOrder, boolean isAdditional) {
        log.info("Trying to send sell order");
        this.lastBuyPrice = lastBuyPrice;
        if (databaseOrderService.getOpenSellOrders().length > 0 && !isAdditional) {
            cancelAllSellOrders();
        }
        double sellPrice = isDemo ? initialPrice : lastBuyPrice;
        for (int i = 0; i < levelOfFilledBuyOrder; i++) {
            sellPrice += fibonacciLevels[i] * step;
            OrderTO orderTO = OrderTO.builder()
                    .symbol(restOrderService.getSymbol())
                    .side(OrderSide.Sell)
                    .orderQty(initialQty * fibonacciLevels[i])
                    .price(sellPrice)
                    .orderType(OrderType.Limit)
                    .build();
            Order order = restOrderService.sendOrder(orderTO)[0];
            log.info("Order with price " + order.getPrice() + " was sent");
            order.setLevelInOrderDB(i + 1);
            addOrderToDatabase(order);
        }
    }

    private void cancelAllSellOrders() {
        Order[] sellOrders = databaseOrderService.getOpenSellOrders();
        if (sellOrders.length > 0) {
            restOrderService.cancelOrders(sellOrders);
            deleteOrdersFromDatabase(sellOrders);
        }
    }

    private void cancelAllBuyOrders() {
        Order[] buyOrders = databaseOrderService.getOpenBuyOrders();
        if (buyOrders.length > 0) {
            restOrderService.cancelOrders(buyOrders);
            deleteOrdersFromDatabase(buyOrders);
        }
    }

    private void sendAdditionalBuyOrder(int levelOfFilledSellOrder) {
        log.info("Trying to send additional buy order");
        int buyOrderQty = 0;
        for (int i = 0; i < levelOfFilledSellOrder; i++) {
            buyOrderQty += fibonacciLevels[i] * initialQty;
        }
        String additionalBuyOrderId = databaseOrderService.getAdditionalBuyOrderId();
        if (additionalBuyOrderId != null) {
            Order[] previousAdditionalOrder = new Order[]{databaseOrderService.getAdditionalBuyOrder(additionalBuyOrderId)};
            deleteOrdersFromDatabase(previousAdditionalOrder);
            restOrderService.cancelOrders(previousAdditionalOrder);
        }
        OrderTO orderTO = OrderTO.builder()
                .symbol(restOrderService.getSymbol())
                .side(OrderSide.Buy)
                .orderQty(buyOrderQty)
                .price(lastBuyPrice)
                .orderType(OrderType.Limit)
                .build();
        Order order = restOrderService.sendOrder(orderTO)[0];
        log.info("Additional buy order with id " + order.getOrderId() + " was sent");
        order.setLevelInOrderDB(levelOfFilledSellOrder);
        databaseOrderService.setAdditionalBuyOrderId(order.getOrderId());
        addOrderToDatabase(order);
    }

    private void addOrderToDatabase(Order order) {
        databaseOrderService.addOrder(order);
    }

    private void deleteOrdersFromDatabase(Order[] orders) {
        for (Order order : orders) {
            databaseOrderService.deleteOrder(order);
        }
    }
    private void deleteOrdersFromDatabase(Order order) {
        databaseOrderService.deleteOrder(order);
    }

    @Override
    public void destroy() throws Exception {
        Order[] openBuyOrders = databaseOrderService.getOpenBuyOrders();
        deleteOrdersFromDatabase(openBuyOrders);
        if (openBuyOrders.length > 0) {
            restOrderService.cancelOrders(openBuyOrders);
        }
        log.info("Application is stopped");
    }
}

