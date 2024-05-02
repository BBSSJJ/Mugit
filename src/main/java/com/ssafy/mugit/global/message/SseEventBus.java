package com.ssafy.mugit.global.message;


import com.ssafy.mugit.global.dto.SseEmitterDto;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Component
public class SseEventBus {
    private final String EMITTER_TIMEOUT;
    private final EmitterRepository emitterRepository;

    public SseEventBus(@Value("${emitter.timeout}") String emitterTimeout,
                       @Autowired EmitterRepository emitterRepository) {
        EMITTER_TIMEOUT = emitterTimeout;
        this.emitterRepository = emitterRepository;
    }

    public SseEmitter subscribe(Long id, String lastEventId) { // {id}.{timestamp}

        // 미수신한 데이터 한번에 전송
        if (!lastEventId.isEmpty()) sendLostEmitter(id, lastEventId);

        return subscribe(id);
    }

    public SseEmitter subscribe(Long id) {

        // 기본값 설정
        return subscribe(id, Long.parseLong(EMITTER_TIMEOUT));
    }

    public SseEmitter subscribe(Long id, Long timeout) {
        // Emitter 생성 후 subscribe
        SseEmitter emitter = new SseEmitter(timeout);
        long createTime = System.currentTimeMillis();
        emitter.onCompletion(() -> emitterRepository.deleteByIdAndCreateTime(id, createTime));
        emitter.onTimeout(() -> emitterRepository.deleteByIdAndCreateTime(id, createTime));

        // Emitter 저장
        emitterRepository.save(id, createTime, emitter);

        return emitter;
    }

    @SneakyThrows
    private void sendLostEmitter(Long id, String lastEventId) {
        Long lastEventCreateTime = Long.parseLong(lastEventId.substring(lastEventId.lastIndexOf(".") + 1));
        List<SseEmitterDto> emitters = emitterRepository.findAllById(id);
        emitters.stream()
                .filter((sseEmitterDto) -> lastEventCreateTime.equals(sseEmitterDto.getCreateTime()))
                .forEach(SseEmitterDto::send);
    }
}
