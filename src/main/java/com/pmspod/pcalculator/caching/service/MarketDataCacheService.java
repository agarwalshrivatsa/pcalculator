package com.pmspod.pcalculator.caching.service;

import com.pmspod.pcalculator.dto.MarketDataDto;

public interface MarketDataCacheService {

    void updateMarketData(String symbol, MarketDataDto marketDataDto);

    MarketDataDto getMarketData(String symbol);
}
