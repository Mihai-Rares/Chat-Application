package com.chatapp.backend.service;

import com.chatapp.backend.dao.UserDAO;
import com.chatapp.backend.model.Channel;
import com.chatapp.backend.model.Message;
import com.chatapp.backend.model.Subscriber;
import com.chatapp.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class SubscriberService {
    private Map<Long, Collection<Subscriber>> topics;
    @Autowired
    UserDAO userDAO;

    public SubscriberService() {
        this.topics = new ConcurrentHashMap<>();
    }

    public synchronized void subscribeToChannel(long channelId, Subscriber subscriber) {
        Collection<Subscriber> subscribers = topics.get(channelId);
        if (subscribers == null) {
            subscribers = new ConcurrentSkipListSet<>();
            topics.put(channelId, subscribers);
        }
        subscribers.add(subscriber);
    }

    public void unsubscribeFromChannel(long channelId, Subscriber subscriber) {
        Collection<Subscriber> subscribers = topics.get(channelId);
        if (subscribers != null) {
            subscribers.remove(subscriber);
            if (subscribers.isEmpty()) {
                topics.remove(channelId);
            }
        }
    }

    public void subscribeUser(String username) {
        User user = userDAO.findByUsername(username);
        Subscriber subscriber = new Subscriber(username);
        for (Channel channel : user.getMemberOf()) {
            subscribeToChannel(channel.getId(), subscriber);
        }
    }

    public void unsubscribeUser(String username) {
        User user = userDAO.findByUsername(username);
        Subscriber subscriber = new Subscriber(username);
        for (Channel channel : user.getMemberOf()) {
            unsubscribeFromChannel(channel.getId(), subscriber);
        }
    }

    public void broadcast(long channelId, Message message) throws IOException {
        Collection<Subscriber> subscribers = topics.get(channelId);
        if (subscribers != null) {
            for (Subscriber subscriber : subscribers) {
                subscriber.sendMessage(message);
            }
        }
    }
}
