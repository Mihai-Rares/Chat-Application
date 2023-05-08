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

/**
 * This class represents a service for managing subscribers and channels.
 */
@Service
public class SubscriberService {
    /**
     * A map that associates a channel ID with a collection of subscribers.
     */
    private Map<Long, Collection<Subscriber>> topics;
    /**
     * The DAO object for accessing user information.
     */
    @Autowired
    UserDAO userDAO;

    /**
     * Creates a new SubscriberService object.
     */
    public SubscriberService() {
        this.topics = new ConcurrentHashMap<>();
    }

    /**
     * Adds a subscriber to the specified channel.
     *
     * @param channelId  The ID of the channel to subscribe to.
     * @param subscriber The subscriber to add to the channel.
     */
    public synchronized void subscribeToChannel(long channelId, Subscriber subscriber) {
        Collection<Subscriber> subscribers = topics.get(channelId);
        if (subscribers == null) {
            subscribers = new ConcurrentSkipListSet<>();
            topics.put(channelId, subscribers);
        }
        subscribers.add(subscriber);
    }

    /**
     * Removes a subscriber from the specified channel.
     *
     * @param channelId  The ID of the channel to unsubscribe from.
     * @param subscriber The subscriber to remove from the channel.
     */
    public void unsubscribeFromChannel(long channelId, Subscriber subscriber) {
        Collection<Subscriber> subscribers = topics.get(channelId);
        if (subscribers != null) {
            subscribers.remove(subscriber);
            if (subscribers.isEmpty()) {
                topics.remove(channelId);
            }
        }
    }

    /**
     * Subscribes a user to all channels they are a member of.
     *
     * @param username The username of the user to subscribe.
     */
    public void subscribeUser(String username) {
        User user = userDAO.findByUsername(username);
        Subscriber subscriber = new Subscriber(username);
        for (Channel channel : user.getMemberOf()) {
            subscribeToChannel(channel.getId(), subscriber);
        }
    }

    /**
     * Unsubscribes a user from all channels they are a member of.
     *
     * @param username The username of the user to unsubscribe.
     */
    public void unsubscribeUser(String username) {
        User user = userDAO.findByUsername(username);
        Subscriber subscriber = new Subscriber(username);
        for (Channel channel : user.getMemberOf()) {
            unsubscribeFromChannel(channel.getId(), subscriber);
        }
    }

    /**
     * Broadcasts a message to all subscribers of a channel.
     *
     * @param channelId The ID of the channel to broadcast the message to.
     * @param message   The message to broadcast.
     * @throws IOException If there was an error while broadcasting the message.
     */
    public void broadcast(long channelId, Message message) throws IOException {
        Collection<Subscriber> subscribers = topics.get(channelId);
        if (subscribers != null) {
            for (Subscriber subscriber : subscribers) {
                subscriber.sendMessage(message);
            }
        }
    }
}
