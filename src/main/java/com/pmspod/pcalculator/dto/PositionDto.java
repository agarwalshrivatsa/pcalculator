package com.pmspod.pcalculator.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PositionDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L; // Add a serialVersionUID

    private String ticker;
    private String totalQty;
    private String avgPrice;
    private String currency;
    private String unrealizedPnl;
    private String lastPrice;
    private String positionId;
}
