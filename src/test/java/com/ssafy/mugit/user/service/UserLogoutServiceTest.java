package com.ssafy.mugit.user.service;

import com.ssafy.mugit.user.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("logout")
class UserLogoutServiceTest {

    CookieUtil cookieUtil = new CookieUtil();

    UserLogoutService sut;

    @BeforeEach
    void setUp() {
        sut = new UserLogoutService(new CookieUtil());
    }

    @Test
    @DisplayName("[통합] 로그아웃 시 쿠키삭제")
    void testDeleteCookie() {
        // given
        HttpSession session = new MockHttpSession();

        // when
        HttpHeaders logout = sut.logout(session);
        List<String> cookieInHeader = logout.get(HttpHeaders.SET_COOKIE);
        HttpHeaders httpHeaders = cookieUtil.deleteLoginCookie();

        // then
        assertThat(cookieInHeader).contains(httpHeaders.getFirst(HttpHeaders.SET_COOKIE));
    }
}