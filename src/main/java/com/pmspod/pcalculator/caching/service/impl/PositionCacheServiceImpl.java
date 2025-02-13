package com.pmspod.pcalculator.caching.service.impl;

import com.pmspod.pcalculator.caching.service.PositionCacheService;
import com.pmspod.pcalculator.dto.PositionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PositionCacheServiceImpl implements PositionCacheService {

    //Todo: add lock?

    @Autowired
    private CacheManager cacheManager;

    @Override
    public PositionDto getPositionByTicker(String ticker) {
        Cache cache = cacheManager.getCache("positions");
        if(cache != null) {
            return cache.get(ticker, PositionDto.class);
        }
        return null;
    }

    @Override
    @CachePut(value = "positions", key = "#ticker")
    public PositionDto updatePosition(String ticker, PositionDto position) {
        return position;
    }

    @Override
    //TODO: check thread safety
    public List<PositionDto> getAllPositions() {
        Cache cache = cacheManager.getCache("positions");
        if(cache != null) {
            ConcurrentHashMap<String, PositionDto> map = (ConcurrentHashMap<String, PositionDto>) cache.getNativeCache();
            return map.values().stream().toList();
        }
        return null;
    }
}
