package com.dmitrii.ostapchuk.bitmexBot.util.handler;

import com.dmitrii.ostapchuk.bitmexBot.model.OrderMessage;
import com.dmitrii.ostapchuk.bitmexBot.model.entity.Order;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class MessageMapper {
    private ObjectMapper objectMapper;
    public Order[] getOrderInfosFromMessage(String message) {
        Order[] orders = null;
        try {
            OrderMessage orderMessage = objectMapper.readValue(message, OrderMessage.class);
            orders = orderMessage.getData();
            log.info("The number of orders mapped from message: " + orders.length);
            return orders;
        } catch (JsonParseException e) {
            log.warn("There is no order info in message");
        } catch (JsonProcessingException e) {
            log.warn("There are issues with JSON parsing");
        }
        return orders;
    }

}
