package com.chatapp.backend.util;

import com.chatapp.backend.dao.ChannelDAO;
import com.chatapp.backend.dao.MessageDAO;
import com.chatapp.backend.dao.UserDAO;
import com.chatapp.backend.model.Channel;
import com.chatapp.backend.model.Message;
import com.chatapp.backend.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @Mock
    private UserDetails userDetails;

    private String token;
    private String username = "testUser";

    @Before
    public void setUp() {
        // Set up a valid token
        token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + JwtUtil.TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, JwtUtil.getSecretKey())
                .compact();

        // Set up mock user details
        when(userDetails.getUsername()).thenReturn(username);
    }

    @Test
    public void testGetUsernameFromToken() {
        String result = jwtUtil.getUsernameFromToken(token);
        assertEquals(username, result);
    }

    @Test
    public void testGetAuthenticationToken() {
        Authentication authentication = jwtUtil.getAuthenticationToken("Bearer " + token);
        assertEquals(username, authentication.getName());
        assertTrue(authentication.getAuthorities().contains(new SimpleGrantedAuthority("USER_ROLE")));
    }

    @Test
    public void testValidateToken() {
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    public void testValidateTokenWithExpiredToken() {
        // Set up an expired token
        String expiredToken = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(SignatureAlgorithm.HS512, JwtUtil.getSecretKey())
                .compact();

        assertFalse(jwtUtil.validateToken(expiredToken, userDetails));
    }
}