package com.chatapp.backend.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public record Subscriber(String username) implements Comparable<Subscriber> {
    @Autowired
    private static SimpMessagingTemplate messagingTemplate;

    public void sendMessage(Message message) throws IOException {
        messagingTemplate.convertAndSendToUser(username, "/queue/new-message", message.toString());
    }

    @Override
    public int compareTo(Subscriber o) {
        return username.compareTo(o.username);
    }
}
