package com.is.chat.service;

import com.is.chat.dao.ChannelDAO;
import com.is.chat.dao.UserDAO;
import com.is.chat.model.Channel;
import com.is.chat.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelDAO channels;
    private final UserDAO userDAO;

    public void addNewChannel(Channel channel) {
        log.info("Recived new user {}", channel.getName());
        channels.save(channel);
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
