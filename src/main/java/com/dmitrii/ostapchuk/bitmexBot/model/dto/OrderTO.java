package com.dmitrii.ostapchuk.bitmexBot.model.dto;

import com.dmitrii.ostapchuk.bitmexBot.model.OrderSide;
import com.dmitrii.ostapchuk.bitmexBot.model.OrderType;
import com.dmitrii.ostapchuk.bitmexBot.model.Symbol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderTO {
    private Symbol symbol;
    private OrderSide side;
    private Integer orderQty;
    private Double price;
    private OrderType orderType;
    private Double stopPx;
}
