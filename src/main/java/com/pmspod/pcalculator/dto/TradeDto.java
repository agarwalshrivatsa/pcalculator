package com.pmspod.pcalculator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TradeDto {

    private String extOrderId;

//    private Long orderId;

    private String tradeDate;

    private String ticker;

    private String exchange;

    private String orderType;

    private String quantity;

    private String price;

    private String currency;

    private String accountId;

    private String positionId;

}
