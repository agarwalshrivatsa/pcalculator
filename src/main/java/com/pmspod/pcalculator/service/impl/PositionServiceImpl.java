package com.pmspod.pcalculator.service.impl;

import com.pmspod.pcalculator.caching.service.MarketDataCacheService;
import com.pmspod.pcalculator.caching.service.PositionCacheService;
import com.pmspod.pcalculator.dto.MarketDataDto;
import com.pmspod.pcalculator.dto.PositionDto;
import com.pmspod.pcalculator.service.PositionService;
import com.pmspod.pcalculator.util.PositionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    }
}
