package com.chatapp.backend.dao;

import com.chatapp.backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing User entities in the database.
 */
@Repository
public interface UserDAO extends CrudRepository<User, Integer> {

    /**
     * Finds a User entity by username.
     *
     * @param username the username to search for
     * @return the User entity with the given username, or null if not found
     */
    User findByUsername(String username);
}
