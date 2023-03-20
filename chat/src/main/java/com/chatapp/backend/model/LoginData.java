package com.chatapp.backend.model;

public record LoginData(String username, String password) {
    public static final LoginData INVALID_LOGIN_DATA = new LoginData(null, null);
}
