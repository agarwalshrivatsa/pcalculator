package com.pmspod.pcalculator.service.impl;

import com.pmspod.pcalculator.caching.service.PositionCacheService;
import com.pmspod.pcalculator.dto.PositionDto;
import com.pmspod.pcalculator.dto.TradeDto;
import com.pmspod.pcalculator.service.TradeProcessingService;
import com.pmspod.pcalculator.util.PositionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TradeProcessingServiceImpl implements TradeProcessingService {

    @Autowired
    private PositionCacheService positionCacheService;

    //Todo: Async?
    @Override
    public synchronized void processTrades(List<TradeDto> tradeList){

        /*
        * for each trade:
        * 1. find position using ticker
        * 2. if no position:
        *    * create new position
        * 3. else:
        *    * update position
         */

        if(tradeList != null) {
            for (TradeDto trade : tradeList) {
                PositionDto position = getExistingPosition(trade.getAccountId(), trade.getTicker());
                if (position == null) {
                    createPosition(trade);
                } else {
                    updatePosition(trade, position);
                }
            }
        }



    }

    private PositionDto getExistingPosition(String accountId, String ticker){
        return positionCacheService.getPositionByTicker(accountId, ticker);
    }

    private void createPosition(TradeDto trade){
        PositionDto position = PositionUtil.getPositionFromTrade(trade);
        positionCacheService.updatePosition(trade.getAccountId(), trade.getTicker(), position);
        log.info("Created new position for trade: {}", trade );
    }


    private void updatePosition(TradeDto tradeDto, PositionDto position){
        PositionDto newPosition = PositionUtil.getNewPosition(position, tradeDto);
        positionCacheService.updatePosition(tradeDto.getAccountId(), tradeDto.getTicker(), newPosition);
        log.info("Updated existing position for trade: {}", tradeDto);
    }

}
