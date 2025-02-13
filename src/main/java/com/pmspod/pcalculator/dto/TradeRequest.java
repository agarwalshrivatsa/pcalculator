package com.pmspod.pcalculator.dto;

import lombok.Data;

import java.util.List;

@Data
public class TradeRequest {

    private List<TradeDto> tradeList;
}
