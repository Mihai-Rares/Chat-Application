package com.chatapp.backend.model;

import com.chatapp.backend.util.JsonUtil;
import com.chatapp.backend.util.json.JSONSerializable;
import com.fasterxml.jackson.core.JsonProcessingException;

public record Notification<T>(String type, T content) implements JSONSerializable {
    @Override
    public String toJSON() {
        try {
            return JsonUtil.toJSON(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
