package com.ssafy.mugit.global.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SseEvent {

    CONNECT("connect"),

    @JsonProperty("follow")
    FOLLOW("follow"),

    @JsonProperty("flow_release")
    FLOW_RELEASE("flow_release"),

    @JsonProperty("like")
    LIKE("like");


    private final String event;
}
