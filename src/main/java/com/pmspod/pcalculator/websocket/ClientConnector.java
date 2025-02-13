package com.pmspod.pcalculator.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Component
@Slf4j
public class ClientConnector implements ApplicationRunner {

    @Autowired
    private WebSocketStompClient stompClient;

    @Autowired
    private StompSessionHandler sessionHandler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("executing application runner for initializing MDS connection");
        stompClient.connectAsync("ws://localhost:8080/portfolio", sessionHandler)
                .thenAccept((session) -> {
                    log.info("Connected to MDS, session ID: " + session.getSessionId());
                });
    }
}
