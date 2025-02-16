package com.pmspod.pcalculator.aggregation;

import com.pmspod.pcalculator.dto.PositionDto;
import com.pmspod.pcalculator.dto.TradeDto;
import com.pmspod.pcalculator.util.PositionUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionUtilTest {

    @Test
    public void testGetNewAvgPrice_returnsCorrectValue_whenOrderTypeIsBuy(){
        PositionDto existingPosition = new PositionDto();
        existingPosition.setPositionId("1");
        existingPosition.setAvgPrice("100.0");
        existingPosition.setCurrency("USD");
        existingPosition.setLastPrice("100.0");
        existingPosition.setTicker("AAPL");
        existingPosition.setTotalQty("100.0");

        TradeDto trade = new TradeDto();
        trade.setCurrency("USD");
        trade.setOrderType("BUY");
        trade.setPrice("200.0");
        trade.setQuantity("100.0");
        trade.setTicker("AAPL");

        String result = PositionUtil.getNewAvgPrice(existingPosition, trade);

        assertEquals("150.0000", result);

        TradeDto trade2 = new TradeDto();
        trade2.setCurrency("USD");
        trade2.setOrderType("BUY");
        trade2.setPrice("339.6");
        trade2.setQuantity("106");
        trade2.setTicker("AAPL");

        String result2 = PositionUtil.getNewAvgPrice(existingPosition, trade2);
        assertEquals("223.2893", result2);
    }

    @Test
    public void testGetNewAvgPrice_returnsCorrectValue_whenValuesPriceIsDecimal(){

        PositionDto existingPosition = new PositionDto();
        existingPosition.setPositionId("1");
        existingPosition.setAvgPrice("66.6666");
        existingPosition.setCurrency("USD");
        existingPosition.setLastPrice("66.6666");
        existingPosition.setTicker("AAPL");
        existingPosition.setTotalQty("100.0");

        TradeDto trade = new TradeDto();
        trade.setCurrency("USD");
        trade.setOrderType("BUY");
        trade.setPrice("98.9650");
        trade.setQuantity("100.0");
        trade.setTicker("AAPL");

        String result = PositionUtil.getNewAvgPrice(existingPosition, trade);
        assertEquals("82.8158", result);
    }

    @Test
    public void testGetNewAvgPrice_returnsCorrectValue_whenFinalTotalQtyIsZero() {
        PositionDto existingPosition = new PositionDto();
        existingPosition.setPositionId("1");
        existingPosition.setAvgPrice("100.0");
        existingPosition.setCurrency("USD");
        existingPosition.setLastPrice("100.0");
        existingPosition.setTicker("AAPL");
        existingPosition.setTotalQty("100.0");

        TradeDto trade = new TradeDto();
        trade.setCurrency("USD");
        trade.setOrderType("SELL");
        trade.setPrice("200.0");
        trade.setQuantity("100.0");
        trade.setTicker("AAPL");

        String result = PositionUtil.getNewAvgPrice(existingPosition, trade);

        assertEquals("0.0000", result);
    }

    @Test
    public void testGetNewAvgPrice_returnsCorrectValue_whenOrderTypeIsSell() {

        PositionDto existingPosition = new PositionDto();
        existingPosition.setPositionId("1");
        existingPosition.setAvgPrice("100.0");
        existingPosition.setCurrency("USD");
        existingPosition.setLastPrice("100.0");
        existingPosition.setTicker("AAPL");
        existingPosition.setTotalQty("100.0");

        TradeDto trade = new TradeDto();
        trade.setCurrency("USD");
        trade.setOrderType("SELL");
        trade.setPrice("200.0");
        trade.setQuantity("50.0");
        trade.setTicker("AAPL");

        String result = PositionUtil.getNewAvgPrice(existingPosition, trade);

        assertEquals("100.0000", result);
    }

    @Test
    public void testGetNewAvgPrice_whenOldQtyIsNegAndTradeTypeSell(){
        PositionDto existingPosition = new PositionDto();
        existingPosition.setPositionId("1");
        existingPosition.setAvgPrice("100.0");
        existingPosition.setCurrency("USD");
        existingPosition.setLastPrice("100.0");
        existingPosition.setTicker("AAPL");
        existingPosition.setTotalQty("-100.0");

        TradeDto trade = new TradeDto();
        trade.setCurrency("USD");
        trade.setOrderType("SELL");
        trade.setPrice("200.0");
        trade.setQuantity("100.0");
        trade.setTicker("AAPL");

        String result = PositionUtil.getNewAvgPrice(existingPosition, trade);

        assertEquals("150.0000", result);

    }

    @Test
    public void testGetNewAvgPrice_whenOldQtyIsPosAndNewQtyNeg(){
        PositionDto existingPosition = new PositionDto();
        existingPosition.setPositionId("1");
        existingPosition.setAvgPrice("100.0");
        existingPosition.setCurrency("USD");
        existingPosition.setLastPrice("100.0");
        existingPosition.setTicker("AAPL");
        existingPosition.setTotalQty("100.0");

        TradeDto trade = new TradeDto();
        trade.setCurrency("USD");
        trade.setOrderType("SELL");
        trade.setPrice("200.0");
        trade.setQuantity("200.0");
        trade.setTicker("AAPL");

        String result = PositionUtil.getNewAvgPrice(existingPosition, trade);

        assertEquals("200.0000", result);

    }






}
