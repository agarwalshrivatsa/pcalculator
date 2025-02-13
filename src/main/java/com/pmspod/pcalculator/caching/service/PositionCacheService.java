package com.pmspod.pcalculator.caching.service;

import com.pmspod.pcalculator.dto.PositionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PositionCacheService {

    public PositionDto getPositionByTicker(String ticker);

    public PositionDto updatePosition(String ticker, PositionDto position);

    public List<PositionDto> getAllPositions();
}
