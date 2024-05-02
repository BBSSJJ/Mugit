package com.ssafy.mugit.global.message;

import com.ssafy.mugit.global.dto.SseEmitterDto;
import com.ssafy.mugit.global.exception.SseException;
import com.ssafy.mugit.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static com.ssafy.mugit.global.exception.error.SseError.SSE_EMITTER_NOT_FOUND;
import static com.ssafy.mugit.user.fixture.UserFixture.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("notification")
class EmitterRepositoryTest {

    EmitterRepository sut;

    @BeforeEach
    void setUp() {
        sut = new EmitterRepository();
    }

    @Test
    @DisplayName("저장되지 않은 Emitter 조회")
    void testFindNotSavedEmitter() {
        // given
        User user = USER.getFixture();

        // when
        assertThatThrownBy(() -> sut.findLastEmitterById(user.getId()))

                // then
                .isInstanceOf(SseException.class)
                .hasMessageContaining(SSE_EMITTER_NOT_FOUND.getMessage());

    }

    @Test
    @DisplayName("Emitter 저장 및 조회")
    void testEmitterFind() throws InterruptedException {
        // given
        User user = USER.getFixture();
        SseEmitter emitter = new SseEmitter(60_000L * 10);

        // when

        sut.save(user.getId(), System.currentTimeMillis(), emitter);
        Thread.sleep(10);
        SseEmitterDto emitterInRepo = sut.findLastEmitterById(user.getId());

        // then
        assertThat(emitterInRepo).isNotNull();
        assertThat(emitterInRepo.getCreateTime()).isNotNull();
        assertThat(emitterInRepo.getEmitter().getTimeout()).isEqualTo(60_000L * 10);
    }

    @Test
    @DisplayName("여러 emitter 중 마지막 조회")
    void testLastEmitterFind() throws InterruptedException {
        // given
        User user = USER.getFixture();

        // when
        SseEmitter emitter = new SseEmitter(60_000L * 10);
        sut.save(user.getId(), System.currentTimeMillis(), emitter);
        Thread.sleep(10);
        SseEmitter emitter2 = new SseEmitter(60_000L * 10);
        sut.save(user.getId(), System.currentTimeMillis(), emitter2);
        Thread.sleep(10);

        SseEmitterDto emitterInRepo = sut.findLastEmitterById(user.getId());

        // then
        assertThat(emitterInRepo).isNotNull();
        assertThat(emitterInRepo.getEmitter()).isEqualTo(emitter2);
        assertThat(emitterInRepo.getEmitter()).isNotEqualTo(emitter);
    }


}