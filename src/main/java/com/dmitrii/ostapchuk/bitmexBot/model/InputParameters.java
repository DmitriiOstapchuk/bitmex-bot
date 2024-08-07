package com.dmitrii.ostapchuk.bitmexBot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class InputParameters {
    private Symbol symbol;
    private int numberOfLevels;
    private int step;
    private int initialQty;
    private int numberOfCycles;
    private String baseUrl;
    private Boolean isDemo;
}
