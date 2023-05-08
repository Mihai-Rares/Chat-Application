package com.chatapp.backend.service;

import com.chatapp.backend.dao.ChannelDAO;
import com.chatapp.backend.dao.UserDAO;
import com.chatapp.backend.model.Channel;
import com.chatapp.backend.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private ChannelDAO channelDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testLoadUserByUsername() {
        // Given

        String username = "testuser";
        String email = "testemail";
        String password = "testpassword";
        Set<Channel> memberOf = new TreeSet<>();

        User user = new User(username, email, password);
        user.setMemberOf(memberOf);

        given(userDAO.findByUsername(username)).willReturn(user);
        // When
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Then
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo("testpassword");
    }

    @Test
    public void loadUserByUsername_whenUserNotFound_throwUsernameNotFoundException() {
        // Given
        String username = "nonexistentuser";

        // When
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(username));

        // Then
        assertThat(exception.getMessage()).isEqualTo("User not found in the database");
    }


    @Test
    public void testGetUserWithUsername() {
        // Given
        String username = "testuser";
        String email = "testemail";
        String password = "testpassword";
        Set<Channel> memberOf = new TreeSet<>();
        User user = new User(username, email, password);
        user.setMemberOf(memberOf);

        given(userDAO.findByUsername(username)).willReturn(user);

        // When
        User result = userService.getUserWithUsername(username);

        // Then
        assertThat(result).isEqualTo(user);
    }

    @Test
    public void testAddNewUser() {
        // Given
        String username = "testuser";
        String email = "testemail";
        String password = "testpassword";
        Set<Channel> memberOf = new TreeSet<>();
        User user = new User(username, email, password);
        user.setMemberOf(memberOf);

        // When
        userService.addNewUser(user);

        // Then
        verify(passwordEncoder).encode("testpassword");
        verify(userDAO).save(user);
    }

    @Test
    public void testGetGroupForUser() {
        // Given
        String username = "testuser";
        String email = "testemail";
        String password = "testpassword";
        Set<Channel> memberOf = new TreeSet<>();
        User user = new User(username, email, password);
        user.setMemberOf(memberOf);

        String groupName = "testgroup";
        Channel channel = new Channel();
        channel.setName(groupName);
        channel.setId(1);
        user.addChannel(channel);

        given(userDAO.findByUsername(username)).willReturn(user);

        // When
        Channel result = userService.getGroupForUser(groupName, username);

        // Then
        assertThat(result).isEqualTo(channel);
    }

    @Test
    public void testGetChannels() {
        // Given
        String username = "testuser";
        String email = "testemail";
        String password = "testpassword";
        Set<Channel> memberOf = new TreeSet<>();
        User user = new User(username, email, password);
        user.setMemberOf(memberOf);

        Channel channel1 = new Channel();
        channel1.setId(1);
        Channel channel2 = new Channel();
        channel2.setId(2);

        user.addChannel(channel1);
        user.addChannel(channel2);

        given(userDAO.findByUsername(username)).willReturn(user);

        // When
        List<Channel> result = userService.getChannels(username);

        // Then
        assertIterableEquals(Arrays.asList(channel1, channel2), result);
    }
}

