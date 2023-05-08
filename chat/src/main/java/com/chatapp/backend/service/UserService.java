package com.chatapp.backend.service;

import com.chatapp.backend.dao.ChannelDAO;
import com.chatapp.backend.dao.UserDAO;
import com.chatapp.backend.model.Channel;
import com.chatapp.backend.model.User;
import lombok.extern.slf4j.Slf4j;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Provides services for managing users in the application.
 */
@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserDAO userDAO;
    private final ChannelDAO channelDAO;
    private final PasswordEncoder passwordEncoder;

    /**
     * Loads the user with the specified username.
     *
     * @param username the username of the user to load
     * @return the user details
     * @throws UsernameNotFoundException if the user with the specified username is not found
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("USER_ROLE"));
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        }
    }

    /**
     * Gets the user with the specified username.
     *
     * @param username the username of the user to get
     * @return the user with the specified username, or null if no such user exists
     */
    public User getUserWithUsername(String username) {
        return userDAO.findByUsername(username);
    }

    /**
     * Adds a new user to the system.
     *
     * @param user the user to add
     */
    public void addNewUser(User user) {
        log.info("Received new user {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.save(user);
    }

    /**
     * Gets the group with the specified name for the specified user.
     *
     * @param groupname the name of the group to get
     * @param username  the username of the user to get the group for
     * @return the group with the specified name for the specified user
     */
    public Channel getGroupForUser(String groupname, String username) {
        return userDAO.findByUsername(username).getGroup(groupname);
    }

    /**
     * Gets the channels that the specified user is a member of.
     *
     * @param username the username of the user to get the channels for
     * @return a list of channels that the specified user is a member of
     */
    public List<Channel> getChannels(String username) {
        return new ArrayList(userDAO.findByUsername(username).getMemberOf());
    }

    /**
     * Gets the groups that the specified user administrates.
     *
     * @param username the username of the user to get the groups for
     * @return a list of groups that the specified user administrates
     */
    public List<Channel> getAdministratedGroups(String username) {
        return new ArrayList(userDAO.findByUsername(username).getMemberOf());
    }

    /**
     * Adds the specified user to the specified channel.
     *
     * @param adminName the username of the administrator adding the user to the channel
     * @param username  the username of the user to add to the channel
     * @param id        the ID of the channel to add the user to
     */
    public void addToChannel(String adminName, String username, long id) {
        User admin = getUserWithUsername(adminName);
        Channel channel = channelDAO.findById(id);
        if (channel.isAdmin(admin)) {
            User u = getUserWithUsername(username);
            u.addChannel(channel);
        }
    }
}
