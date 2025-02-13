package com.pmspod.pcalculator.service;

import com.pmspod.pcalculator.dto.MarketDataDto;
import org.springframework.stereotype.Service;

@Service
public interface MarketDataHandlerService {

    void handleMarketData(MarketDataDto marketDataDto);
}
