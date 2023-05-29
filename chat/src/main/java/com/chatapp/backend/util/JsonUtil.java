package com.chatapp.backend.util;

import com.chatapp.backend.util.json.JSONSerializable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.chatapp.backend.model.LoginData;
import com.chatapp.backend.util.json.RequestWrapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class JsonUtil {
    private static final JsonUtil singleton = new JsonUtil();

    public LoginData getLoginData(HttpServletRequest request) {
        LoginData data;
        try {
            RequestWrapper wrapper = new RequestWrapper(request);
            byte[] body = StreamUtils.copyToByteArray(wrapper.getInputStream());
            Map<String, Object> jsonRequest = new ObjectMapper().readValue(body, Map.class);
            String username = (String) jsonRequest.get("username");
            String password = (String) jsonRequest.get("password");
            if (username == null || password == null) {
                data = LoginData.INVALID_LOGIN_DATA;
            } else {
                data = new LoginData(username, password);
            }

        } catch (IOException e) {
            data = LoginData.INVALID_LOGIN_DATA;
        }
        return data;
    }

    public static <T extends JSONSerializable> String getJSON(Collection<T> list) {
        StringBuilder json = new StringBuilder("[ ");
        boolean first = true;
        for (T m : list) {
            if (first) {
                first = false;
                json.append(m.toJSON());
            } else {
                json.append(" , ").append(m);
            }
        }
        return json.append(" ]").toString();
    }

    public static <T> String toJSON(T obj) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(obj);
        return json;
    }

    @Bean
    public static JsonUtil getSingleton() {
        return singleton;
    }
}
