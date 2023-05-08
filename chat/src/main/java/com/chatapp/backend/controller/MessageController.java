package com.chatapp.backend.controller;

import com.chatapp.backend.model.Message;
import com.chatapp.backend.service.MessageService;
import com.chatapp.backend.service.SubscriberService;
import com.chatapp.backend.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final SubscriberService subscriberService;
    private final JsonUtil jsonUtil = JsonUtil.getSingleton();

    @GetMapping("/messagesForChannel")
    public String getMessagesForChannel(Principal principal, @RequestBody long channelId) {
        ArrayList<Message> messages = new ArrayList<>(messageService.getMessagesForChannel(principal.getName(), channelId));
        return jsonUtil.getJSON(messages);
    }

    @GetMapping("/messages")
    public String getMessages(Principal principal) {
        ArrayList<Message> messages = new ArrayList<>(messageService.getMessages(principal.getName()));
        messages.sort(Comparator.comparingLong(Message::getId));
        return jsonUtil.getJSON(messages);
    }

    private static class MessageInfo {
        public long to;
        public String text;
    }

    @PostMapping("/sendMessage")
    public void sendMessage(Principal principal, @RequestBody MessageInfo messageInfo) {
        Message message = messageService.sendMessage(principal.getName(), messageInfo.to, messageInfo.text);
        try {
            subscriberService.broadcast(messageInfo.to, message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class NewMessageRequest {
        public long channelId;
        public long messageId;
    }

    @PostMapping("getNewMessages")
    public String getNewMessages(Principal principal, @RequestBody NewMessageRequest newMessageRequest) {
        ArrayList<Message> messages =
                new ArrayList<>(messageService
                        .getNewMessages(principal.getName(),
                                newMessageRequest.channelId, newMessageRequest.messageId));
        messages.sort(Comparator.comparingLong(Message::getId));
        return jsonUtil.getJSON(messages);
    }
}
