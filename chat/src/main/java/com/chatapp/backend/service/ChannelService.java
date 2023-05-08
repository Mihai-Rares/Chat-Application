package com.chatapp.backend.service;

import com.chatapp.backend.dao.ChannelDAO;
import com.chatapp.backend.dao.UserDAO;
import com.chatapp.backend.model.Channel;
import com.chatapp.backend.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Service class for managing channels.
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelDAO channels;
    private final UserDAO userDAO;

    /**
     * Saves a new channel to the database.
     *
     * @param channel the channel to save
     */
    public void addNewChannel(Channel channel) {
        log.info("Received new channel {}", channel.getName());
        channels.save(channel);
    }

    /**
     * Returns a collection of all the members for the specified channel.
     *
     * @param channel_id the ID of the channel to get the members for
     * @return a collection of users who are members of the specified channel
     */
    public Collection<User> getMembersForChannel(int channel_id) {
        return null;
    }

    /**
     * Adds a user to the specified channel.
     *
     * @param user      the user to add to the channel
     * @param channelId the ID of the channel to add the user to
     */
    public void addUser(User user, int channelId) {
    }

    /**
     * Returns the channel with the specified ID.
     *
     * @param id the ID of the channel to get
     * @return the channel with the specified ID
     */
    public Channel getChannelById(int id) {
        return channels.findById(id);
    }

    /**
     * Returns a collection of all the administrators for the specified channel.
     *
     * @param channel the channel to get the administrators for
     * @return a collection of users who are administrators of the specified channel
     */
    public Collection<User> getAdminsForChannel(Channel channel) {
        return null;
    }
}

