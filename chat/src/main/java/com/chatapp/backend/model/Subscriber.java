package com.chatapp.backend.model;

import org.springframework.web.socket.WebSocketSession;

public record Subscriber(long id, WebSocketSession session) {
}
