package com.pmspod.pcalculator.service.impl;

import com.pmspod.pcalculator.caching.service.MarketDataCacheService;
import com.pmspod.pcalculator.caching.service.PositionCacheService;
import com.pmspod.pcalculator.dto.MarketDataDto;
import com.pmspod.pcalculator.dto.PositionDto;
import com.pmspod.pcalculator.dto.TradeDto;
import com.pmspod.pcalculator.service.PositionService;
import com.pmspod.pcalculator.util.PositionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionCacheService positionCacheService;

    @Autowired
    private MarketDataCacheService mdcService;

    @Override
    public List<PositionDto> getAllPositions() {
        List<PositionDto> allPositions = positionCacheService.getAllPositions();
        allPositions.forEach(this::updatePosition);
        return allPositions;
    }

    public void updatePosition(PositionDto positionDto){
        MarketDataDto md = mdcService.getMarketData(positionDto.getTicker());
        String uPnl = PositionUtil.getUnrealizedPnL(positionDto, md);
        positionDto.setLastPrice(Float.toString(md.getTradePrice()));
        positionDto.setUnrealizedPnl(uPnl);

        // Calculate additional PnL metrics
        calculateAdditionalPnL(positionDto, md);
    }

    /**
     * Calculates additional PnL metrics for a position.
     * Uses simplified calculations based on available data.
     *
     * @param position The position to update
     * @param marketData Current market data
     */
    private void calculateAdditionalPnL(PositionDto position, MarketDataDto marketData) {
        BigDecimal currentPrice = BigDecimal.valueOf(marketData.getTradePrice());

        // For demonstration, we'll simulate having trades and previous prices
        // In a real implementation, you would retrieve these from your data source
        List<TradeDto> simulatedTrades = getSimulatedTradesForPosition(position.getPositionId());
        BigDecimal previousDayPrice = estimatePreviousDayPrice(currentPrice);
        BigDecimal yearStartPrice = estimateYearStartPrice(currentPrice);

        // Daily realized PnL - using the existing method
        position.setDailyRealizedPnl(PositionUtil.getDailyRealizedPnL(position, simulatedTrades));

        // Daily unrealized PnL - price change since yesterday
        position.setDailyUnrealizedPnl(PositionUtil.getDailyUnrealizedPnL(
                position, currentPrice, previousDayPrice));

        // YTD PnL - simplified estimate
        position.setYtdPnl(PositionUtil.getYtdPnL(
                position, simulatedTrades, currentPrice, yearStartPrice));

        // ITD PnL - simplified estimate
        position.setItdPnl(PositionUtil.getItdPnL(
                position, simulatedTrades, currentPrice));

        // Add logging here
        System.out.println("Position: " + position.getTicker() + ", Qty: " + position.getTotalQty());
        System.out.println("YTD PnL: " + position.getYtdPnl());
        System.out.println("ITD PnL: " + position.getItdPnl());
    }

    /**
     * Creates simulated trades for testing/demonstration purposes.
     * In a real implementation, this would be replaced with actual trade data retrieval.
     */
    private List<TradeDto> getSimulatedTradesForPosition(String positionId) {
        // This is just a placeholder - in a real implementation, you would
        // retrieve actual trades from your database or another service
        return new ArrayList<>();
    }

    /**
     * Estimates the previous day's price based on current price.
     * In a real implementation, this would be replaced with actual historical data.
     */
    private BigDecimal estimatePreviousDayPrice(BigDecimal currentPrice) {
        // Simulate yesterday's price as 0.5% different from today
        return currentPrice.multiply(new BigDecimal("0.995"));
    }

    /**
     * Estimates the year start price based on current price.
     * In a real implementation, this would be replaced with actual historical data.
     */
    private BigDecimal estimateYearStartPrice(BigDecimal currentPrice) {
        // Simulate year start price as 10% different from current price
        return currentPrice.multiply(new BigDecimal("0.90"));
    }

}