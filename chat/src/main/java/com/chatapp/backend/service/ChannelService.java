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

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelDAO channels;
    private final UserDAO userDAO;

    public Channel addNewChannel(Channel channel) {
        log.info("Recived new user {}", channel.getName());
        return channels.save(channel);
    }

    public Collection<User> getMembersForChannel(int channel_id) {
        return null;
    }

    public void addUser(User user, int channelId) {
    }

    public Channel getChannelById(int id) {
        return channels.findById(id);
    }

    public Collection<User> getAdminsForChannel(Channel channel) {
        return null;
    }
}
