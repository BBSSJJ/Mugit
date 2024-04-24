package com.ssafy.mugit.auth.service;

import com.ssafy.mugit.auth.SessionKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
class SessionServiceTest {

    SessionService sut;

    @BeforeEach
    void setUp() {
        sut = new SessionService();
    }

    @Test
    @DisplayName("[단위] 세션 생성")
    void testCreateSession() {
        // given
        HttpServletRequest request = new MockHttpServletRequest();
        Long id = 1L;
        
        // when
        HttpSession session = sut.createSession(request, id);

        // then
        assertThat(session).isNotNull();
        assertThat(session.isNew()).isTrue();
        assertThat(session.getId()).isNotNull();
        assertThat(session.getAttribute(SessionKeys.LOGIN_USER_ID.getKey())).isEqualTo(1L);
    }
    
    @Test
    @DisplayName("[단위] 세션 값 확인")
    void testSaveSession() {
        // given
        Long id = 1L;
        HttpServletRequest request = new MockHttpServletRequest();
        sut.createSession(request, 1L);

        // when
        Long idInSession = sut.getSession(request);
        
        // then
        assertThat(idInSession).isEqualTo(1L);
    }

}