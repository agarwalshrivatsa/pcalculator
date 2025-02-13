package com.pmspod.pcalculator.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmspod.pcalculator.dto.MarketDataDto;
import com.pmspod.pcalculator.service.MarketDataHandlerService;
import com.pmspod.pcalculator.service.impl.MarketDataHandlerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Slf4j
public class MdsSessionHandler implements StompSessionHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MarketDataHandlerService marketDataHandlerService;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("Connected to MDS. Subscribing topics");
        session.subscribe("/topic/v1", this);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("Error in MDS", exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {

    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return String.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {

        try{
            MarketDataDto marketDataDto = mapper.readValue(payload.toString(), MarketDataDto.class);
            marketDataHandlerService.handleMarketData(marketDataDto);
//            log.info("Received message from MDS: " + marketDataDto);
        } catch (Exception e) {
            log.error("Error in parsing message from MDS", e);
        }


    }
}
