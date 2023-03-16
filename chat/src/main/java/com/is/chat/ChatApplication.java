package com.is.chat;

import com.is.chat.controller.ChannelController;
import com.is.chat.dao.ChannelDAO;
import com.is.chat.model.Channel;
import com.is.chat.model.User;
import com.is.chat.service.ChannelService;
import com.is.chat.service.MessageService;
import com.is.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;

@SpringBootApplication
public class ChatApplication {
    private final ChannelDAO channelDAO;

    public ChatApplication(ChannelDAO channelDAO) {
        this.channelDAO = channelDAO;
    }

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService, ChannelService channelService,
                          MessageService messageService) {
        loadUsers(userService);
        return args -> userService.addNewUser(new User("username", "email", "password"));
    }

    void loadUsers(UserService userService) {
        userService.addNewUser(new User("Robert Brown", "email", "password"));
        userService.addNewUser(new User("Susan Miller", "email", "password"));
    }

    void loadChannels(ChannelService channelService, UserService userService) {
        Channel c = new Channel();
        c.setName("");
        c.setMembers(new HashSet<>());
        c.addMember(userService.getUserWithUsername("Robert Brown"));
        c.addMember(userService.getUserWithUsername("Susan Miller"));
        channelService.addNewChannel(c);
    }
}
