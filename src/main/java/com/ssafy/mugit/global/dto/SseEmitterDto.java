package com.ssafy.mugit.global.dto;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Getter
@ToString
public class SseEmitterDto<T> implements Comparable<SseEmitterDto> {
    private String id;
    private Long createTime;
    private SseEmitter emitter;
    private T message;

    public SseEmitterDto(Long userId, Long createTime, SseEmitter emitter) {
        this.id = userId + "." + createTime;
        this.createTime = createTime;
        this.emitter = emitter;
    }

    @Override
    public int compareTo(SseEmitterDto o) {
        return this.createTime.compareTo(o.createTime);
    }

    public void send() {
        try {
            emitter.send(SseEmitter.event().id(id).name("notification").data(message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
