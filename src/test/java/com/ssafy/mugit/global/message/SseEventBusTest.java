package com.ssafy.mugit.global.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.dto.SseEmitterDto;
import com.ssafy.mugit.global.exception.SseException;
import com.ssafy.mugit.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static com.ssafy.mugit.global.exception.error.SseError.SSE_EMITTER_NOT_FOUND;
import static com.ssafy.mugit.user.fixture.UserFixture.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("notification")
@SpringBootTest
class SseEventBusTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EmitterRepository emitterRepository;

    @Autowired
    SseEventBus sut;

    @Test
    @DisplayName("[통합] Emitter 생성 및 저장, 조회")
    void TestCreateEmitter() {
        // given
        User user = USER.getFixture();

        // when
        sut.subscribe(user.getId());
        SseEmitterDto emitterDto = emitterRepository.findLastEmitterById(user.getId());

        // then
        assertThat(emitterDto).isNotNull();
        assertThat(emitterDto.getEmitter().getTimeout()).isEqualTo(60_000L * 60);
    }
    
    @Test
    @DisplayName("[통합] Emitter 삭제 테스트")
    void TestDeleteEmitter() throws InterruptedException {
        // given
        User user = USER.getFixture();

        // when
        sut.subscribe(user.getId());
        emitterRepository.findLastEmitterById(user.getId()).getEmitter().complete();
        SseEmitterDto lastEmitter = emitterRepository.findLastEmitterById(user.getId());

        // then
        assertThat(lastEmitter).isNull();
        assertThatThrownBy(() -> emitterRepository.findLastEmitterById(user.getId()))
                .isInstanceOf(SseException.class)
                .hasMessage(SSE_EMITTER_NOT_FOUND.getMessage());
    }
}