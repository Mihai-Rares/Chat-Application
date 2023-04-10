package com.chatapp.backend.controller;

import com.chatapp.backend.model.Message;
import com.chatapp.backend.service.ChatService;
import com.chatapp.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;


    private static class MessageInfo {
        public long to;
        public String text;
    }

    @MessageMapping("/chat/send-message")
    public void sendMessage(MessageInfo messageInfo, Principal principal) {
        String username = principal.getName();
        messageService.sendMessage(username, messageInfo.to, messageInfo.text);
        chatService.sendMessage(messageInfo.to, username, messageInfo.text);
    }
}