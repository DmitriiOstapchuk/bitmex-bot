package com.dmitrii.ostapchuk.bitmexBot.model.dto;

import com.dmitrii.ostapchuk.bitmexBot.model.OrderSide;
import com.dmitrii.ostapchuk.bitmexBot.model.OrderStatus;
import com.dmitrii.ostapchuk.bitmexBot.model.OrderType;
import com.dmitrii.ostapchuk.bitmexBot.model.Symbol;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

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
