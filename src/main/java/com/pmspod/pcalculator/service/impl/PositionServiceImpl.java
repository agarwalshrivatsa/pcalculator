package com.pmspod.pcalculator.service.impl;

import com.pmspod.pcalculator.caching.service.MarketDataCacheService;
import com.pmspod.pcalculator.caching.service.PositionCacheService;
import com.pmspod.pcalculator.dto.MarketDataDto;
import com.pmspod.pcalculator.dto.PositionDto;
import com.pmspod.pcalculator.entity.Position;
import com.pmspod.pcalculator.mapper.PositionMapper;
import com.pmspod.pcalculator.repo.PositionsRepo;
import com.pmspod.pcalculator.service.PositionService;
import com.pmspod.pcalculator.util.PositionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionCacheService positionCacheService;

    @Autowired
    private MarketDataCacheService mdcService;

    @Autowired
    private PositionsRepo positionsRepo;

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

    @Scheduled(fixedDelay = 720000)
    public void updatePositions(){
        List<PositionDto> positions = getAllPositions();
        List<Position> updatedPositions = positions.stream().map(PositionMapper::map).toList();

        positionsRepo.saveAll(updatedPositions);

        //TODO: Enable scheduling

    }


}
