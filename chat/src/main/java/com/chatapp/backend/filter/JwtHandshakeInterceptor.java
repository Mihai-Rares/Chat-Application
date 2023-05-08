package com.chatapp.backend.filter;

import com.chatapp.backend.util.JsonUtil;
import com.chatapp.backend.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * An interceptor to handle WebSocket handshake requests and authenticate users with JWT tokens.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtUtil util;

    /**
     * Called before the WebSocket handshake is completed.
     *
     * @param request    the current request
     * @param response   the current response
     * @param wsHandler  the WebSocketHandler that will handle the WebSocket messages
     * @param attributes attributes from the HTTP handshake to associate with the WebSocket session
     * @return true if the handshake should proceed, false otherwise
     * @throws Exception if an error occurs while handling the handshake
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String token = servletRequest.getServletRequest().getParameter("token");
            if (token != null) {
                try {
                    SecurityContextHolder.getContext().setAuthentication(
                            util.getAuthenticationToken("Bearer " + token));
                } catch (Exception exception) {
                    log.error("Error logging in: {}", exception.getMessage());
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Called after the WebSocket handshake is successfully completed.
     *
     * @param request   the current request
     * @param response  the current response
     * @param wsHandler the WebSocketHandler that will handle the WebSocket messages
     * @param exception an exception that occurred after the handshake (may be null)
     */
    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // Empty implementation
    }
}
