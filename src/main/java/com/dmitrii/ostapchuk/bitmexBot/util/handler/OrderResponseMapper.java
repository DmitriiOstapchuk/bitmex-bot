package com.dmitrii.ostapchuk.bitmexBot.util.handler;

import com.dmitrii.ostapchuk.bitmexBot.model.entity.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.net.http.HttpResponse;
@Component
@Slf4j
@AllArgsConstructor
public class OrderResponseMapper {
    private ObjectMapper objectMapper;

    public Order[] handleResponse(HttpResponse<String> response) {
        Order[] orders = null;
        if (response.statusCode() != 200) {
            log.error("Response has status code " + response.statusCode() + " and couldn't be handled");
        } else {
            log.info("New request was successfully sent!");
            String responseStr = response.body();
            try {
                orders = objectMapper.readValue(responseStr, Order[].class);
                for (Order order: orders) {
                    log.info("Order " + order.getOrderId() + " was successfully extracted from response");
                }
                return orders;
            } catch (JsonProcessingException e) {
                log.warn("It's not an array, trying to handle it as single order");
            }
            try {
                log.info("ResponseStr is " + responseStr);
                Order order = objectMapper.readValue(responseStr, Order.class);
                log.info("Order " + order.getOrderId() + " was successfully extracted from response");
                orders = new Order[]{order};
            } catch (JsonProcessingException e) {
                log.warn("There is a problem with parsing json to an order");
            }
        }
        return orders;
    }
}