package com.chatapp.backend.service;


import com.chatapp.backend.dao.AccountDAO;
import com.chatapp.backend.dao.ChannelDAO;
import com.chatapp.backend.dao.MessageDAO;
import com.chatapp.backend.dao.UserDAO;
import com.chatapp.backend.exception.NotFoundException;
import com.chatapp.backend.model.Account;
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
public class AccountServiceTest {
    @Mock
    private AccountDAO accountDAO;
    @InjectMocks
    private AccountService accountService;
    private Account account;
    private User user;

    @Before
    public void init() {
        String username = "testuser";
        String email = "testemail";
        String password = "testpassword";
        Set<Channel> memberOf = new TreeSet<>();
        user = new User(username, email, password);
        user.user_id = 1;
        user.setMemberOf(memberOf);

        account = new Account();
        account.setExtension("extension");
        account.setName("account");
        account.setDescription("description");
        account.setUser(user);
        account.setId(1L);
    }

    @Test
    public void updateDescriptionTest() {
        //given
        String description = "new description";
        Optional<Account> optional = Optional.of(account);
        given(accountDAO.findById(1L)).willReturn(optional);
        account.setDescription(description);
        given(accountDAO.save(account)).willReturn(account);

        //when
        Account result = accountService.updateDescription(1L, description);

        //then
        assertThat(result).isEqualTo(account);
    }

    @Test
    public void updateDescriptionFailTest() {
        //given
        String description = "new description";
        Optional<Account> optional = Optional.empty();
        given(accountDAO.findById(1L)).willReturn(optional);

        //when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> accountService.updateDescription(1L, description));

        //then
        assertThat(exception.getMessage()).isEqualTo("Account not found with id: " + 1L);
    }
}
