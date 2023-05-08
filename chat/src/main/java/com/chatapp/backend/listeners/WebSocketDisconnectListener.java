package com.chatapp.backend.listeners;

import com.chatapp.backend.service.SubscriberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

/**
 * Component class that listens for WebSocket disconnection events.
 */
@Component
@Slf4j
public class WebSocketDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {
    @Autowired
    SubscriberService subscriberService;

    /**
     * Called when a WebSocket session is disconnected.
     *
     * @param event The WebSocket session disconnect event.
     */
    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = sha.getUser();
        log.info("New user disconnected {}", principal.getName());
        subscriberService.unsubscribeUser(principal.getName());
    }
}

