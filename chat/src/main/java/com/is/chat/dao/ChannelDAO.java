package com.is.chat.dao;

import com.is.chat.model.Channel;
import com.is.chat.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ChannelDAO extends CrudRepository<Channel, Long> {
    Channel findById(long channel_id);
}
