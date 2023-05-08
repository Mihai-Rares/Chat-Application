package com.chatapp.backend.listeners;

import com.chatapp.backend.service.SubscriberService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

/**
 * Listener for WebSocket session connect events.
 * Subscribes a user upon connection.
 */
@Component
@Slf4j
public class WebSocketConnectListener implements ApplicationListener<SessionConnectEvent> {

    @Autowired
    SubscriberService subscriberService;

    /**
     * Called when a WebSocket session is connected.
     * Subscribes the user associated with the session.
     *
     * @param event the connect event
     */
    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = sha.getUser();
        log.info("New user connected {}", principal.getName());
        subscriberService.subscribeUser(principal.getName());
    }

}




