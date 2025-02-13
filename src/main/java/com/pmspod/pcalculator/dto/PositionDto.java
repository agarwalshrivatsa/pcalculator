package com.pmspod.pcalculator.dto;

import lombok.Data;

@Data
public class PositionDto {

    private String ticker;
    private String totalQty;
    private String avgPrice;
    private String currency;
    private String unrealizedPnl;
    private String lastPrice;
    private String positionId;
}
