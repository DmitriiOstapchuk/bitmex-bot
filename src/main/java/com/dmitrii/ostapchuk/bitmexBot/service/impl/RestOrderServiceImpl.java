package com.dmitrii.ostapchuk.bitmexBot.service.impl;


import com.dmitrii.ostapchuk.bitmexBot.model.*;
import com.dmitrii.ostapchuk.bitmexBot.model.dto.OrderTO;
import com.dmitrii.ostapchuk.bitmexBot.model.entity.Order;
import com.dmitrii.ostapchuk.bitmexBot.service.DatabaseOrderService;
import com.dmitrii.ostapchuk.bitmexBot.service.RestOrderService;
import com.dmitrii.ostapchuk.bitmexBot.util.Endpoints;
import com.dmitrii.ostapchuk.bitmexBot.util.generator.DataGenerator;
import com.dmitrii.ostapchuk.bitmexBot.util.generator.HttpRequestGenerator;
import com.dmitrii.ostapchuk.bitmexBot.util.generator.SignatureGenerator;
import com.dmitrii.ostapchuk.bitmexBot.util.handler.OrderResponseMapper;
import com.dmitrii.ostapchuk.bitmexBot.util.handler.ActualPriceExtractor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class RestOrderServiceImpl implements RestOrderService {
    private final HttpClient httpClient;
    private final SignatureGenerator signatureGenerator;
    private final DataGenerator dataGenerator;
    private final HttpRequestGenerator httpRequestGenerator;
    private final DatabaseOrderService databaseOrderService;
    private final OrderResponseMapper orderResponseMapper;
    private final ActualPriceExtractor actualPriceExtractor;
    private String apiKey;
    private String apiSecretKey;
    private String baseUrl;
    private Symbol symbol;
    private final int EXPIRES_DELAY = 5;

    public Double getActualPrice() {
        String data = "";
        return sendTradeRequest("GET", data, Endpoints.getActualPriceEndpoint(Symbol.XBTUSD, OrderSide.Sell));
    }
    public Order[] sendOrder(OrderTO orderTO) {
        String data = dataGenerator.generateDataForPostRequest(orderTO);
        return sendOrderRequest("POST", data);
    }

    public void cancelOrders(Order[] orders) {
        sendOrderRequest("DELETE", dataGenerator.generateDataForDeleteRequest(orders));
    }

    public void getAllOrders () {
        String data = "";
        sendOrderRequest("GET", "");
    }
    public boolean getOpenOrders (Symbol symbol) {
        String data = "";
        String openOrdersEndpoint = Endpoints.getOpenOrdersEndpoint(symbol);
        Order[] existingOrders = sendOrderRequest("GET", data, openOrdersEndpoint);
        return existingOrders.length == 0 ? false : true;
    }

    public Order[] sendOrderRequest(String httpMethod, String data) {
        String endpoint = Endpoints.ORDER_ENDPOINT;
        return sendOrderRequest(httpMethod, data, endpoint);
    }
    public Order[] sendOrderRequest(String httpMethod, String data, String endpoint) {
        HttpResponse<String> response = sendRequestToEndpoint(httpMethod, data, endpoint);
        return orderResponseMapper.handleResponse(response);
    }

    public Double sendTradeRequest(String httpMethod, String data, String endpoint) {
        HttpResponse<String> response = sendRequestToEndpoint(httpMethod, data, endpoint);
        return actualPriceExtractor.extractActualPrice(response);
    }

    private HttpResponse<String> sendRequestToEndpoint (String httpMethod, String data, String endpoint) {
        log.info("Try to send new " + httpMethod + " request...");
        AuthenticationHeaders authenticationHeaders = null;
        try {
            authenticationHeaders = this.getAuthenticationHeaders(httpMethod, data, endpoint);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.warn("There is some issues with getting authentication headers");
        }
        HttpRequest httpRequest = httpRequestGenerator.createHttpRequest(baseUrl, endpoint, data, httpMethod, authenticationHeaders);
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(response.body());
        return response;
    }


    public AuthenticationHeaders getAuthenticationHeaders(String httpMethod, String data, String path) throws NoSuchAlgorithmException, InvalidKeyException {
        String expires = String.valueOf(System.currentTimeMillis() / 1000 + EXPIRES_DELAY);

        String signature = signatureGenerator.getSignature(apiSecretKey, httpMethod + path + expires + data);

        return AuthenticationHeaders.builder()
                .apiKey(apiKey)
                .signature(signature)
                .expires(expires)
                .build();
    }

}
