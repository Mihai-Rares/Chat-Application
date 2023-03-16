package com.is.chat.controller;

import com.is.chat.model.Channel;
import com.is.chat.model.User;
import com.is.chat.service.UserService;
import com.is.chat.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JsonUtil jsonUtil = JsonUtil.getSingleton();

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody User user) {
        userService.addNewUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addToGroup")
    public void addUserToChannel(Principal principal, @RequestBody String username, @RequestBody long id) {
        userService.addToChannel(principal.getName(), username, id);
    }

    @GetMapping("/channels")
    public String getChannels(Principal principal) {
        return jsonUtil.getJSON(userService.getChannels(principal.getName()));
    }

    @GetMapping("/administratedGroups")
    public String getAdministratedGroups(Principal principal) {
        return jsonUtil.getJSON(userService.getAdministratedGroups(principal.getName()));
    }

    @PostMapping("/getGroup")
    public String getChannel(Principal principal, @RequestBody String name) {
        return userService.getGroupForUser(name, principal.getName()).toString();
    }
}
