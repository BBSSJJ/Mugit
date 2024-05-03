package com.ssafy.mugit.global.dto;

public enum DataKeys {
    LOGIN_USER_KEY("user");

    private final String key;

    DataKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getSessionKey() {
        return "sessionAttr:" + key;
    }
}
