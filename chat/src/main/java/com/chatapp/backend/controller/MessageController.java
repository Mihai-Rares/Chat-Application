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


/**
 * Controller class for handling message-related API requests.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final SubscriberService subscriberService;
    private final JsonUtil jsonUtil = JsonUtil.getSingleton();

    /**
     * Handles GET request for retrieving all messages for a given channel.
     *
     * @param principal the current user's authentication object.
     * @param channelId the ID of the channel to retrieve messages for.
     * @return a JSON string representing the list of messages.
     */
    @GetMapping("/messagesForChannel")
    public String getMessagesForChannel(Principal principal, @RequestBody long channelId) {
        ArrayList<Message> messages = new ArrayList<>(messageService.getMessagesForChannel(principal.getName(), channelId));
        return jsonUtil.getJSON(messages);
    }

    /**
     * Handles GET request for retrieving all messages for the current user.
     *
     * @param principal the current user's authentication object.
     * @return a JSON string representing the list of messages.
     */
    @GetMapping("/messages")
    public String getMessages(Principal principal) {
        ArrayList<Message> messages = new ArrayList<>(messageService.getMessages(principal.getName()));
        messages.sort(Comparator.comparingLong(Message::getId));
        return jsonUtil.getJSON(messages);
    }

    /**
     * Handles POST request for sending a new message.
     *
     * @param principal   the current user's authentication object.
     * @param messageInfo an object containing the recipient ID and message text.
     */
    @PostMapping("/sendMessage")
    public void sendMessage(Principal principal, @RequestBody MessageInfo messageInfo) {
        Message message = messageService.sendMessage(principal.getName(), messageInfo.to, messageInfo.text);
        try {
            subscriberService.broadcast(messageInfo.to, message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles POST request for retrieving new messages for a given channel.
     *
     * @param principal         the current user's authentication object.
     * @param newMessageRequest an object containing the channel ID and the ID of the last received message.
     * @return a JSON string representing the list of new messages.
     */
   /* @PostMapping("getNewMessages")
    public String getNewMessages(Principal principal, @RequestBody NewMessageRequest newMessageRequest) {
        ArrayList<Message> messages =
                new ArrayList<>(messageService
                        .getNewMessages(principal.getName(),
                                newMessageRequest.channelId, newMessageRequest.messageId));
        messages.sort(Comparator.comparingLong(Message::getId));
        return jsonUtil.getJSON(messages);
    }*/

    /**
     * Class representing the JSON object expected in the request body for sending a message.
     */
    private static class MessageInfo {
        public long to;
        public String text;
    }

    /**
     * Class representing the JSON object expected in the request body for retrieving new messages.
     */
    private static class NewMessageRequest {
        public long channelId;
        public long messageId;
    }
}
