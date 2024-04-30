package com.ssafy.mugit.auth;

public enum SessionKeys {
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
