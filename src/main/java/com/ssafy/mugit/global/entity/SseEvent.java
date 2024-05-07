package com.ssafy.mugit.global.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SseEvent {
    CONNECT("connect"),
    @JsonProperty("follow")
    FOLLOW("follow");

    private final String event;
}
