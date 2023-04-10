package com.chatapp.backend.service;

import com.chatapp.backend.model.Channel;
import com.chatapp.backend.model.User;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ChatService {

    private final Map<String, Set<String>> channelSubscribers = new ConcurrentHashMap<>();

    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(long channel, String username, String message) {
        messagingTemplate.convertAndSend("/topic/" + channel, message);
    }

    public void startConversation(Channel conversation) {
        String json = conversation.toString();
        for (User user : conversation.getMembers()) {
            messagingTemplate.convertAndSendToUser(user.username, "/newConversation", json);
        }
    }
}
