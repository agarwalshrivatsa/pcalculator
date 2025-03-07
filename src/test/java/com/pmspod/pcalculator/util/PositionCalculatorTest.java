package com.pmspod.pcalculator.util;

import com.pmspod.pcalculator.dto.MarketDataDto;
import com.pmspod.pcalculator.dto.PositionDto;

import com.pmspod.pcalculator.dto.TradeDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionUtilTest {

    @Test
    void testLongPositionWithProfit() {
        // Setup: Long position (buy) with current price higher than avg price
        PositionDto position = new PositionDto();
        position.setAvgPrice("100.00");
        position.setTotalQty("10");  // Positive quantity means long position

        MarketDataDto marketData = new MarketDataDto();
        marketData.setTradePrice(105.00f);  // Current price is higher

        // Expected: (100 * 10) - (10 * 105) = 1000 - 1050 = -50
        // Since totalQty > 0, multiply by -1 => 50
        String result = PositionUtil.getUnrealizedPnL(position, marketData);

        assertEquals("50.0000", result);
    }

    @Test
    void testLongPositionWithLoss() {
        // Setup: Long position with current price lower than avg price
        PositionDto position = new PositionDto();
        position.setAvgPrice("100.00");
        position.setTotalQty("10");

        MarketDataDto marketData = new MarketDataDto();
        marketData.setTradePrice(95.00f);  // Current price is lower

        // Expected: (100 * 10) - (10 * 95) = 1000 - 950 = 50
        // Since totalQty > 0, multiply by -1 => -50
        String result = PositionUtil.getUnrealizedPnL(position, marketData);

        assertEquals("-50.0000", result);
    }

    @Test
    void testShortPositionWithProfit() {
        // Setup: Short position (sell) with current price lower than avg price
        PositionDto position = new PositionDto();
        position.setAvgPrice("100.00");
        position.setTotalQty("-10");  // Negative quantity means short position

        MarketDataDto marketData = new MarketDataDto();
        marketData.setTradePrice(95.00f);  // Current price is lower

        // Expected: (100 * -10) - (-10 * 95) = -1000 - (-950) = -1000 + 950 = -50
        // Since netPosition < 0, multiply by -1 => 50
        String result = PositionUtil.getUnrealizedPnL(position, marketData);

        assertEquals("50.0000", result);
    }

    @Test
    void testShortPositionWithLoss() {
        // Setup: Short position with current price higher than avg price
        PositionDto position = new PositionDto();
        position.setAvgPrice("100.00");
        position.setTotalQty("-10");

        MarketDataDto marketData = new MarketDataDto();
        marketData.setTradePrice(105.00f);  // Current price is higher

        // Expected: (100 * -10) - (-10 * 105) = -1000 - (-1050) = -1000 + 1050 = 50
        // Since netPosition < 0, multiply by -1 => -50
        String result = PositionUtil.getUnrealizedPnL(position, marketData);

        assertEquals("-50.0000", result);
    }


    @Test
    void testGetYtdPnLForLongPosition() {
        // Setup a long position
        PositionDto position = new PositionDto();
        position.setTotalQty("100");
        position.setAvgPrice("150.00");

        List<TradeDto> ytdTrades = new ArrayList<>();
        BigDecimal currentPrice = new BigDecimal("160.00");
        BigDecimal yearStartPrice = new BigDecimal("140.00");

        // Calculate YTD PnL
        String result = PositionUtil.getYtdPnL(position, ytdTrades, currentPrice, yearStartPrice);

        // Expected: 100 * (150 - 160) = -1000, then negate for long position = 1000
        assertEquals("1000.0000", result);
    }

    @Test
    void testGetYtdPnLForShortPosition() {
        // Setup a short position
        PositionDto position = new PositionDto();
        position.setTotalQty("-100");
        position.setAvgPrice("150.00");

        List<TradeDto> ytdTrades = new ArrayList<>();
        BigDecimal currentPrice = new BigDecimal("140.00");
        BigDecimal yearStartPrice = new BigDecimal("160.00");

        // Calculate YTD PnL
        String result = PositionUtil.getYtdPnL(position, ytdTrades, currentPrice, yearStartPrice);

        // Expected: -100 * (150 - 140) = -1000, then negate for negative net position = 1000
        assertEquals("1000.0000", result);
    }

    @Test
    void testGetItdPnLForLongPosition() {
        // Setup a long position
        PositionDto position = new PositionDto();
        position.setTotalQty("100");
        position.setAvgPrice("150.00");

        List<TradeDto> allTrades = new ArrayList<>();
        BigDecimal currentPrice = new BigDecimal("160.00");

        // Calculate ITD PnL
        String result = PositionUtil.getItdPnL(position, allTrades, currentPrice);

        // Expected: 100 * (150 - 160) = -1000, then negate for long position = 1000
        assertEquals("1000.0000", result);
    }

    @Test
    void testGetItdPnLForShortPosition() {
        // Setup a short position
        PositionDto position = new PositionDto();
        position.setTotalQty("-100");
        position.setAvgPrice("150.00");

        List<TradeDto> allTrades = new ArrayList<>();
        BigDecimal currentPrice = new BigDecimal("140.00");

        // Calculate ITD PnL
        String result = PositionUtil.getItdPnL(position, allTrades, currentPrice);

        // Expected: -100 * (150 - 140) = -1000, then negate for negative net position = 1000
        assertEquals("1000.0000", result);
    }
}