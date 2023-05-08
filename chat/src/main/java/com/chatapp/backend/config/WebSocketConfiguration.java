package com.chatapp.backend.config;

import com.chatapp.backend.filter.JwtHandshakeInterceptor;
import com.chatapp.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configures WebSocket support for the application.
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil util;

    /**
     * Registers the endpoint for WebSocket connections and configures
     * the handshake interceptor to authenticate the user connecting to
     * the WebSocket.
     *
     * @param registry the StompEndpointRegistry to use
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stream/message-flux")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new JwtHandshakeInterceptor(util))
                .withSockJS();
    }

    /**
     * Configures the message broker used by the application.
     *
     * @param registry the MessageBrokerRegistry to use
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/stream/topic", "/stream/user");
        registry.setUserDestinationPrefix("/stream/user");
        registry.setApplicationDestinationPrefixes("/stream/app");
    }
}