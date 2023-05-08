package com.chatapp.backend.service;


import com.chatapp.backend.dao.ChannelDAO;
import com.chatapp.backend.dao.MessageDAO;
import com.chatapp.backend.dao.UserDAO;
import com.chatapp.backend.model.Channel;
import com.chatapp.backend.model.Message;
import com.chatapp.backend.model.User;
import org.junit.Before;
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
public class MessageServiceTest {
    @Mock
    private MessageDAO messages;
    @Mock
    private UserDAO userDAO;
    @Mock
    private ChannelDAO channelDAO;
    @InjectMocks
    private MessageService messageService;
    private User user;
    private Channel channel;
    private Message message;
    private User user2;

    @Before
    public void init() {
        String username = "testuser";
        String email = "testemail";
        String password = "testpassword";
        Set<Channel> memberOf = new TreeSet<>();
        user = new User(username, email, password);
        user.user_id = 1;
        user.setMemberOf(memberOf);

        String groupName = "testchannel";
        Set<User> members = new TreeSet<>();
        members.add(user);
        channel = new Channel();
        channel.setName(groupName);
        channel.setId(1);
        channel.setMembers(members);
        user.addChannel(channel);

        message = new Message(1, "text", 1, user, channel);
    }

    @Test
    public void sendMessageTest() {
        //given
        Message m = new Message(0, "text", 1, user, channel);
        given(userDAO.findByUsername("testuser")).willReturn(user);
        given(channelDAO.findById(1)).willReturn(channel);
        given(messages.save(m)).willReturn(message);

        //when
        Message result = messageService.sendMessage("testuser", 1, "text");

        //then
        verify(messages).save(m);
        assertThat(result).isEqualTo(message);
    }

    @Test
    public void sendMessageFailTest() {
        //given
        Message m = new Message(0, "text", 1, user, channel);
        given(userDAO.findByUsername("testuser")).willReturn(user);
        given(channelDAO.findById(2)).willReturn(null);
        given(messages.save(m)).willReturn(message);

        //when
        Message result = messageService.sendMessage("testuser", 2, "text");

        //then
        assertThat(result).isEqualTo(null);
    }

    @Test
    public void getMessagesForChannelTest() {
        //given
        Message m = new Message(0, "text", 1, user, channel);
        given(userDAO.findByUsername("testuser")).willReturn(user);
        given(channelDAO.findById(1)).willReturn(channel);
        given(messages.findMessagesByTo(channel)).willReturn(List.of(message));

        //when
        List<Message> result = messageService.getMessagesForChannel("testuser", 1);

        //then
        assertIterableEquals(result, List.of(message));
    }
}
