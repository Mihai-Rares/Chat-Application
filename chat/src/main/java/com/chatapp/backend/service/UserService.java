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

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserDAO userDAO;
    private final ChannelDAO channelDAO;
    private PasswordEncoder passwordEncoder;

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

    public User getUserWithUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public void addNewUser(User user) {
        log.info("Recived new user {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.save(user);
    }

    public Channel getGroupForUser(String groupname, String username) {
        return userDAO.findByUsername(username).getGroup(groupname);
    }

    public List<Channel> getChannels(String username) {
        return new ArrayList(userDAO.findByUsername(username).getMemberOf());
    }

    public List<Channel> getAdministratedGroups(String username) {
        return new ArrayList(userDAO.findByUsername(username).getMemberOf());
    }

    public void addToChannel(String adminName, String username, long id) {
        User admin = getUserWithUsername(adminName);
        Channel channel = channelDAO.findById(id);
        if (channel.isAdmin(admin)) {
            User u = getUserWithUsername(username);
            u.addChannel(channel);
        }
    }
}