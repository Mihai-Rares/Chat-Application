package com.chatapp.backend.dao;

import com.chatapp.backend.model.Channel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * This interface provides CRUD operations for the {@link Channel} entity.
 */
@Repository
public interface ChannelDAO extends CrudRepository<Channel, Long> {

    /**
     * Finds a channel by its ID.
     *
     * @param channel_id the ID of the channel to find
     * @return the channel with the specified ID, or {@code null} if no such channel exists
     */
    Channel findById(long channel_id);

}

