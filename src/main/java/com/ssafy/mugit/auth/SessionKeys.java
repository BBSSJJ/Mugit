package com.ssafy.mugit.auth;

public enum SessionKeys {
    LOGIN_USER_ID("userId");

    private String key;

    SessionKeys(String userId) {
        this.key = userId;
    }

    public String getKey() {
        return key;
    }
}
