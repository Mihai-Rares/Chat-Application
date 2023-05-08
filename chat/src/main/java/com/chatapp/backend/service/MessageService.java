package com.chatapp.backend.service;

import com.chatapp.backend.dao.ChannelDAO;
import com.chatapp.backend.dao.MessageDAO;
import com.chatapp.backend.dao.UserDAO;
import com.chatapp.backend.model.Channel;
import com.chatapp.backend.model.Message;
import com.chatapp.backend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageDAO messages;
    private final UserDAO userDAO;
    private final ChannelDAO channelDAO;

    public Message sendMessage(String from, long to, String text) {
        User sender = userDAO.findByUsername(from);
        Channel channel = channelDAO.findById(to);
        if (channel != null && channel.isMember(sender)) {
            Message message = new Message();
            message.setFrom(sender);
            message.setTo(channel);
            message.setText(text);
            message.setDate(System.currentTimeMillis());
            message = messages.save(message);
            return message;
        }
        return null;
    }

    public List<Message> getMessagesForChannel(String username, long channelId) {
        Channel channel = channelDAO.findById(channelId);
        User user = userDAO.findByUsername(username);
        if (channel == null || !channel.isMember(user)) {
            return List.of();
        }
        return messages.findMessagesByTo(channel);
    }

    public List<Message> getMessages(String username) {
        User user = userDAO.findByUsername(username);
        return messages.getMessages(user);
    }

    /*public List<Message> getNewMessages(String username, long channelId, long messageId) {
        Channel channel = channelDAO.findById(channelId);
        User user = userDAO.findByUsername(username);
        if (channel == null || !channel.isMember(user)) {
            return List.of();
        }
        return messages.findMessagesByToAndMessage_idGreaterThan(channel, messageId);
    }*/
}
