package com.chatapp.backend.controller;

import com.chatapp.backend.model.Channel;
import com.chatapp.backend.model.Notification;
import com.chatapp.backend.model.User;
import com.chatapp.backend.service.ChannelService;
import com.chatapp.backend.service.SubscriberService;
import com.chatapp.backend.service.UserService;
import com.chatapp.backend.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;

/**
 * This controller class is responsible for handling requests related to user management.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ChannelService channelService;
    private final JsonUtil jsonUtil = JsonUtil.getSingleton();
    private final SubscriberService subscriberService;

    /**
     * Registers a new user.
     *
     * @param user the user to be registered
     * @return a ResponseEntity with no content and status code 200 (OK) if the user is registered successfully
     */
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody User user) {
        userService.addNewUser(user);
        return ResponseEntity.ok().build();
    }

    private record AddUserToGroupData(String username, int id) {
    }

    /**
     * Adds a user to a channel.
     *
     * @param principal the principal of the current user
     * @param data      the username of the user to be added
     *                  and the ID of the channel to which the user will be added
     */
    @PostMapping("/addToGroup")
    public void addUserToChannel(Principal principal, @RequestBody AddUserToGroupData data) {
        User user = userService.getUserWithUsername(principal.getName());
        User newMember = userService.getUserWithUsername(data.username());
        Channel channel = channelService.getChannelById(data.id());
        if (channel.isAdmin(user)) {
            record NewMemberData(long channel_id, User account) {
            }
            subscriberService.subscribeToChannel(data.id(), data.username());
            subscriberService.broadcastNotification(data.id(),
                    new Notification("NEW_GROUP_MEMBER", new NewMemberData(data.id(), newMember)));
        }
        userService.addToChannel(principal.getName(), data.username(), data.id());

    }

    /**
     * Gets all the channels for the current user.
     *
     * @param principal the principal of the current user
     * @return a JSON string representing a list of channels
     */
    @GetMapping("/channels")
    public Collection<Channel> getChannels(Principal principal) {
        return userService.getChannels(principal.getName());
    }

    /**
     * Gets all the channels administrated by the current user.
     *
     * @param principal the principal of the current user
     * @return a JSON string representing a list of channels administrated by the current user
     */
    @GetMapping("/administratedGroups")
    public Collection<Channel> getAdministratedGroups(Principal principal) {
        return userService.getAdministratedGroups(principal.getName());
    }

    /**
     * Gets a channel with a given name for the current user.
     *
     * @param principal the principal of the current user
     * @param name      the name of the channel to retrieve
     * @return a JSON string representing the channel with the given name for the current user
     */
    @PostMapping("/getGroup")
    public String getChannel(Principal principal, @RequestBody String name) {
        return userService.getGroupForUser(name, principal.getName()).toString();
    }
}
