package com.pmspod.pcalculator.controller;

import com.pmspod.pcalculator.dto.TradeRequest;
import com.pmspod.pcalculator.service.TradeProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/trades")
public class TradeController {

    @Autowired
    private TradeProcessingService tradeProcessingService;

    @PostMapping("upload")
    public ResponseEntity<String> uploadTrades(@RequestBody TradeRequest request){
        log.info("Received trades from Trade Processor: " + request.getTradeList());
        tradeProcessingService.processTrades(request.getTradeList());
        return new ResponseEntity<>("{\"message\":\"Success\"}", HttpStatusCode.valueOf(200));
    }
}
