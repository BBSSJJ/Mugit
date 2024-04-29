package com.ssafy.mugit.auth;

public enum SessionKeys {
    LOGIN_USER_ID("userId"),
    LOGIN_USER_ROLE("role"),
    LOGIN_USER_EMAIL("email"),
    LOGIN_USER_KEY("user");

    private final String key;

    private final String SessionPrefix = "sessionAttr:";

    SessionKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getSessionKey() {
        return SessionPrefix + key;
    }
}
