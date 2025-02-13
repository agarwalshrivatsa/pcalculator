package com.pmspod.pcalculator.service.impl;

import com.pmspod.pcalculator.caching.service.PositionCacheService;
import com.pmspod.pcalculator.dto.PositionDto;
import com.pmspod.pcalculator.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionCacheService positionCacheService;

    @Override
    public List<PositionDto> getAllPositions() {
        List<PositionDto> allPositions = positionCacheService.getAllPositions();
        return allPositions;
    }
}
