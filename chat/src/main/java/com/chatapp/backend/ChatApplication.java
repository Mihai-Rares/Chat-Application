package com.chatapp.backend;

import com.chatapp.backend.dao.ChannelDAO;
import com.chatapp.backend.model.Channel;
import com.chatapp.backend.model.User;
import com.chatapp.backend.service.ChannelService;
import com.chatapp.backend.service.MessageService;
import com.chatapp.backend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
        return args -> userService.addNewUser(new User("John Doe", "email", "password"));
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
