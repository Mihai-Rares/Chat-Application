package com.chatapp.backend.controller;

import com.chatapp.backend.model.User;
import com.chatapp.backend.service.UserService;
import com.chatapp.backend.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * This controller class is responsible for handling requests related to user management.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JsonUtil jsonUtil = JsonUtil.getSingleton();

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

    /**
     * Adds a user to a channel.
     *
     * @param principal the principal of the current user
     * @param username  the username of the user to be added
     * @param id        the ID of the channel to which the user will be added
     */
    @PostMapping("/addToGroup")
    public void addUserToChannel(Principal principal, @RequestBody String username, @RequestBody long id) {
        userService.addToChannel(principal.getName(), username, id);
    }

    /**
     * Gets all the channels for the current user.
     *
     * @param principal the principal of the current user
     * @return a JSON string representing a list of channels
     */
    @GetMapping("/channels")
    public String getChannels(Principal principal) {
        return jsonUtil.getJSON(userService.getChannels(principal.getName()));
    }

    /**
     * Gets all the channels administrated by the current user.
     *
     * @param principal the principal of the current user
     * @return a JSON string representing a list of channels administrated by the current user
     */
    @GetMapping("/administratedGroups")
    public String getAdministratedGroups(Principal principal) {
        return jsonUtil.getJSON(userService.getAdministratedGroups(principal.getName()));
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
