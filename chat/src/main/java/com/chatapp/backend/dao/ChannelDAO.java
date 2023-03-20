package com.chatapp.backend.dao;

import com.chatapp.backend.model.Channel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelDAO extends CrudRepository<Channel, Long> {
    Channel findById(long channel_id);
}
