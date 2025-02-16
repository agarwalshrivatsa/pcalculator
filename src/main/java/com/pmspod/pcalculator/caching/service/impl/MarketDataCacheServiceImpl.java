package com.pmspod.pcalculator.caching.service.impl;

import com.pmspod.pcalculator.caching.service.MarketDataCacheService;
import com.pmspod.pcalculator.dto.MarketDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class MarketDataCacheServiceImpl implements MarketDataCacheService {

    @Autowired
    private CacheManager cacheManager;

    @Override
    @CachePut(value = "marketData", key = "#symbol")
    public MarketDataDto updateMarketData(String symbol, MarketDataDto marketDataDto) {
        return marketDataDto;
    }

    @Override
    public MarketDataDto getMarketData(String symbol) {
        Cache cache = cacheManager.getCache("marketData");
        if(cache != null) {
            return cache.get(symbol, MarketDataDto.class);
        }
        return null;
    }
}
