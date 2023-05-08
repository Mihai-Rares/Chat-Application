package com.chatapp.backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.chatapp.backend.model.LoginData;
import com.chatapp.backend.util.json.RequestWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    public <T> String getJSON(List<T> list) {
        StringBuilder json = new StringBuilder("[ ");
        boolean first = true;
        for (T m : list) {
            if (first) {
                first = false;
                json.append(m);
            } else {
                json.append(" , ").append(m);
            }
        }
        return json.append(" ] ").toString();
    }

    @Bean
    public static JsonUtil getSingleton() {
        return singleton;
    }
}
