package com.dmitrii.ostapchuk.bitmexBot.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LastSellTradeInfoTO {
        private Date timestamp;
        private String symbol;
        private String side;
        private double size;
        private double price;
        private String tickDirection;
        private String trdMatchID;
        private double grossValue;
        private double homeNotional;
        private double foreignNotional;
        private String trdType;
}
