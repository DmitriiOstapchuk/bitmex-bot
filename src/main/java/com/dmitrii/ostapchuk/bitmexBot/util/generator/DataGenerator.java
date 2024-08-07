package com.dmitrii.ostapchuk.bitmexBot.util.generator;

import com.dmitrii.ostapchuk.bitmexBot.model.entity.Order;
import com.dmitrii.ostapchuk.bitmexBot.model.dto.DataForDeleteRequestTO;
import com.dmitrii.ostapchuk.bitmexBot.model.dto.OrderTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@AllArgsConstructor
@Slf4j
public class DataGenerator {
    private ObjectMapper objectMapper;
    public String generateDataForPostRequest(OrderTO orderTO) {
        try {
            return objectMapper.writeValueAsString(orderTO);
        } catch (JsonProcessingException e) {
            log.warn("There is a problem with processing string to JSON");
            throw new RuntimeException(e);
        }
    }

    public String generateDataForDeleteRequest (Order[] orders) {
        String[] orderIds = Arrays.stream(orders)
                .map(order -> order.getOrderId())
                .toArray(String[]::new);
        DataForDeleteRequestTO dataForDeleteRequestTO = new DataForDeleteRequestTO(orderIds);
        try {
            return objectMapper.writeValueAsString(dataForDeleteRequestTO);
        } catch (JsonProcessingException e) {
            log.warn("There is a problem with processing string to JSON");
            throw new RuntimeException(e);
        }
    }
}
