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
    
    private String dailyRealizedPnl;
    private String dailyUnrealizedPnl;
    private String ytdPnl;              // Year-to-date PnL
    private String itdPnl;
}
