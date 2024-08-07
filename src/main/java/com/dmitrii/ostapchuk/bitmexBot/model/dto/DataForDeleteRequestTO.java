package com.dmitrii.ostapchuk.bitmexBot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataForDeleteRequestTO {
    String[] orderID;
}
