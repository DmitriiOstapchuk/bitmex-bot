package com.dmitrii.ostapchuk.bitmexBot.util;

import com.dmitrii.ostapchuk.bitmexBot.model.OrderSide;
import com.dmitrii.ostapchuk.bitmexBot.model.Symbol;

public class Endpoints {

    public static final String ORDER_ENDPOINT = "/api/v1/order";


    public static String getActualPriceEndpoint (Symbol symbol, OrderSide orderSide) {
        String symbolStr = symbol.name();
        String orderSideStr = orderSide.name();
        return "/api/v1/trade?symbol=" + symbolStr + "&filter=%7B%22side%22%3A%20%22" + orderSideStr + "%22%7D&count=1&reverse=true";
    }

    public static String getOpenOrdersEndpoint (Symbol symbol) {
        String symbolStr = symbol.name();
        return "/api/v1/order?symbol=" + symbolStr + "&filter=%7B%22open%22%3A%20true%7D";
    }
}
