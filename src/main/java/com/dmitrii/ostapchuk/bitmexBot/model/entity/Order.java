package com.dmitrii.ostapchuk.bitmexBot.model.entity;

import com.dmitrii.ostapchuk.bitmexBot.model.OrderSide;
import com.dmitrii.ostapchuk.bitmexBot.model.OrderStatus;
import com.dmitrii.ostapchuk.bitmexBot.model.OrderType;
import com.dmitrii.ostapchuk.bitmexBot.model.Symbol;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    @JsonProperty("orderID")
    private String orderId;
    private Symbol symbol;
    private OrderSide side;
    private Integer orderQty;
    private Double price;
    @JsonProperty("ordType")
    private OrderType orderType;
    private Double stopPx;
    @JsonProperty("ordStatus")
    private OrderStatus status;
    private int levelInOrderDB;
}
