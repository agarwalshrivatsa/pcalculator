package com.pmspod.pcalculator.service;

import com.pmspod.pcalculator.dto.TradeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TradeProcessingService {
    public void processTrades(List<TradeDto> tradeDtoList);
}
