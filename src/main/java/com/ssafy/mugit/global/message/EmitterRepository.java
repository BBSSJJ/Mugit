package com.ssafy.mugit.global.message;

import com.ssafy.mugit.global.dto.SseEmitterDto;
import com.ssafy.mugit.global.exception.SseException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ssafy.mugit.global.exception.error.SseError.SSE_EMITTER_NOT_FOUND;

@Component
public class EmitterRepository {

    private final Map<Long, List<SseEmitterDto>> emitterRepository = new ConcurrentHashMap<>();

    public void save(Long id, Long createTime, SseEmitter emitter) {

        // Emitter 저장
        if (!emitterRepository.containsKey(id)) emitterRepository.put(id, new ArrayList<>());

        emitterRepository.get(id).add(new SseEmitterDto(id, createTime, emitter));
    }

    public SseEmitterDto findLastEmitterById(Long id) {

        // Emitter 없을 때 오류
        if (!emitterRepository.containsKey(id)) throw new SseException(SSE_EMITTER_NOT_FOUND);

        return emitterRepository.get(id).stream().max(SseEmitterDto::compareTo)
                .orElseThrow(() -> new SseException(SSE_EMITTER_NOT_FOUND));
    }

    public void deleteByIdAndCreateTime(Long id, Long createTime) {

        // Emitter 없을 때 오류
        List<SseEmitterDto> sseEmitterDtos = emitterRepository.get(id);
        if (sseEmitterDtos == null) throw new SseException(SSE_EMITTER_NOT_FOUND);

        sseEmitterDtos.forEach((emitter) -> {
            if(emitter.getCreateTime().equals(createTime)) sseEmitterDtos.remove(emitter);
        });
    }

    public List<SseEmitterDto> findAllById(Long id) {
        if (!emitterRepository.containsKey(id)) throw new SseException(SSE_EMITTER_NOT_FOUND);

        return emitterRepository.get(id);
    }
}
