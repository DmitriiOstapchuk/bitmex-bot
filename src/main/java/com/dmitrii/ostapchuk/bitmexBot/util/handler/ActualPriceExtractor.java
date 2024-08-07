package com.dmitrii.ostapchuk.bitmexBot.util.handler;


import com.dmitrii.ostapchuk.bitmexBot.model.dto.LastSellTradeInfoTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.http.HttpResponse;
@Component
@AllArgsConstructor
@Slf4j
public class ActualPriceExtractor {
    private ObjectMapper objectMapper;
    public Double extractActualPrice(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            log.warn("Actual price couldn't be extracted. Status code is " + response.statusCode());
            return null;
        } else {
            log.info("Trying to extract actual price");
            String responseStr = response.body();
            log.info("ResponseStr is " + responseStr);
            String jsonString = responseStr.substring(1, responseStr.length()-1);
            log.info("jsonString is " + jsonString);
            double actualPrice = 0;
            try {
                LastSellTradeInfoTO lastSellTradeInfoTO = objectMapper.readValue(jsonString, LastSellTradeInfoTO.class);
                actualPrice = lastSellTradeInfoTO.getPrice();
                log.info("Actual Price is " + actualPrice);
            } catch (JsonProcessingException e) {
                log.warn("Actual price wasn't extracted from JSON string");
            }
            return actualPrice;
        }
    }
}
