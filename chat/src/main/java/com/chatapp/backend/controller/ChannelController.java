package com.chatapp.backend.controller;

import com.chatapp.backend.model.Channel;
import com.chatapp.backend.model.User;
import com.chatapp.backend.service.ChannelService;
import com.chatapp.backend.service.ChatService;
import com.chatapp.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    private final UserService userService;
    private final ChatService chatService;

    @PostMapping("/makeGroup")
    public void makeGroup(Principal principal, @RequestBody String name) {
        User user = userService.getUserWithUsername(principal.getName());
        Channel channel = new Channel();
        channel.setName(name);
        Set<User> members = new HashSet<>();
        Set<User> admins = new HashSet<>();
        members.add(user);
        admins.add(user);
        channel.setMembers(members);
        channel.setAdmins(admins);
        channelService.addNewChannel(channel);
    }

    @PostMapping("/startConversation")
    public void startConversation(Principal principal, @RequestBody String contactedUsername) {
        log.info("Starting conversation: {}", contactedUsername);
        User user = userService.getUserWithUsername(principal.getName());
        User contactedUser = userService.getUserWithUsername(contactedUsername);
        if (contactedUser != null && user != null) {
            Channel channel = new Channel();
            channel.setName("");
            Set<User> members = new HashSet<>();
            members.add(user);
            members.add(contactedUser);
            channel.setMembers(members);
            Channel savedChannel = channelService.addNewChannel(channel);
            chatService.startConversation(savedChannel);
        }
    }
}
