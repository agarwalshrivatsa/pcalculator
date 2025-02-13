package com.pmspod.pcalculator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketDataDto {

    private String date;
    private String time;
    private String symbol;
    private float tradePrice;
    private int tradeSize;
    private float askPrice;
    private float bidPrice;
    private int askSize;
    private int bidSize;
    private String exchange;
    private String currency;

}
