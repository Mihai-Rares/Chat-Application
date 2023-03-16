package com.is.chat.service;

import com.is.chat.dao.ChannelDAO;
import com.is.chat.dao.MessageDAO;
import com.is.chat.dao.UserDAO;
import com.is.chat.model.Channel;
import com.is.chat.model.Message;
import com.is.chat.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageDAO messages;
    private final UserDAO userDAO;
    private final ChannelDAO channelDAO;

    public void sendMessage(String from, long to, String text) {
        User sender = userDAO.findByUsername(from);
        Channel channel = channelDAO.findById(to);
        if (channel != null && channel.isMember(sender)) {
            Message message = new Message();
            message.setFrom(sender);
            message.setTo(channel);
            message.setText(text);
            message.setDate(System.currentTimeMillis());
            messages.save(message);
        }
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

    public List<Message> getNewMessages(String username, long channelId, long messageId) {
        Channel channel = channelDAO.findById(channelId);
        User user = userDAO.findByUsername(username);
        if (channel == null || !channel.isMember(user)) {
            return List.of();
        }
        return messages.findMessagesByToAndMessage_idGreaterThan(channel, messageId);
    }
}
