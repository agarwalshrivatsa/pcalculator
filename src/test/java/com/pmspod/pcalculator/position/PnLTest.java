package com.pmspod.pcalculator.position;

import com.pmspod.pcalculator.dto.MarketDataDto;
import com.pmspod.pcalculator.dto.PositionDto;
import com.pmspod.pcalculator.util.PositionUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PnLTest {

    @Test
    public void testGetUnrealizedPnl_whenLongPositionAndMakingProfit(){

        PositionDto existingPosition = new PositionDto();
        existingPosition.setPositionId("1");
        existingPosition.setAvgPrice("100.0");
        existingPosition.setCurrency("USD");
        existingPosition.setLastPrice("100.0");
        existingPosition.setTicker("AAPL");
        existingPosition.setTotalQty("100.0");

        MarketDataDto marketData = new MarketDataDto();
        marketData.setCurrency("USD");
        marketData.setSymbol("AAPL");
        marketData.setTradePrice(-200.0f);

        String result = PositionUtil.getUnrealizedPnL(existingPosition, marketData);

        assertEquals("-30000.0000", result);
    }
}
