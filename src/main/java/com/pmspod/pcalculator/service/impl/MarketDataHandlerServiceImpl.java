package com.pmspod.pcalculator.service.impl;

import com.pmspod.pcalculator.caching.service.MarketDataCacheService;
import com.pmspod.pcalculator.dto.MarketDataDto;
import com.pmspod.pcalculator.service.MarketDataHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MarketDataHandlerServiceImpl implements MarketDataHandlerService {

    @Autowired
    private MarketDataCacheService marketDataCacheService;

    @Override
    public void handleMarketData(MarketDataDto marketDataDto) {
//        log.info("Updating market data: {}",  marketDataDto.toString());
        marketDataCacheService.updateMarketData(marketDataDto.getSymbol(), marketDataDto);
    }
}
