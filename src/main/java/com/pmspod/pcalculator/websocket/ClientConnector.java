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
        int maxRetries = 10;
        int retry = 0;
        boolean connected = false;

        while (!connected && retry < maxRetries) {
            try {
                log.info("Attempting to connect to MDS (Attempt {}/{})", retry + 1, maxRetries);
                stompClient.connectAsync("ws://mds:8080/portfolio", sessionHandler)
                        .thenAccept((session) -> {
                            log.info("Connected to MDS, session ID: " + session.getSessionId());
                        });
                connected = true;
            } catch (Exception e) {
                log.error("Error connecting to MDS, retrying in 5 seconds", e);
                Thread.sleep(5000);
                retry++;
            }
        }
        
        if (!connected) {
            log.error("Failed to connect to MDS after {} attempts", maxRetries);
        }
    }
}
