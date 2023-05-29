package com.chatapp.backend.util;

import com.chatapp.backend.dao.ChannelDAO;
import com.chatapp.backend.dao.MessageDAO;
import com.chatapp.backend.dao.UserDAO;
import com.chatapp.backend.model.Channel;
import com.chatapp.backend.model.Message;
import com.chatapp.backend.model.User;
import com.chatapp.backend.util.json.JSONSerializable;
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
public class JsonUtilTest {

    private JsonUtil jsonUtil = new JsonUtil();

    @Test
    public void testGetJSON() {
        JSONSerializable one = () -> "one";
        JSONSerializable two = () -> "two";
        JSONSerializable three = () -> "three";
        List<JSONSerializable> list = Arrays.asList(one, two, three);
        String result = jsonUtil.getJSON(list);
        assertEquals("[ one , two , three ] ", result);
    }
}