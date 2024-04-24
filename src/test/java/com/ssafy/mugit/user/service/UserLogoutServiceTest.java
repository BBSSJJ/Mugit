package com.ssafy.mugit.user.service;

import com.ssafy.mugit.user.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

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
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie[] cookies = new Cookie[4];
        cookies[0] = new Cookie("isLogined", "true");
        cookies[1] = new Cookie("nickName", "leaf");
        cookies[2] = new Cookie("profileText", "프로필");
        cookies[3] = new Cookie("profileImage", "test/1234");
        request.setCookies(cookies);

        // when
        HttpHeaders logout = sut.logout(request);
        List<String> cookieInHeader = logout.get(HttpHeaders.SET_COOKIE);
        HttpHeaders httpHeaders = cookieUtil.deleteLoginCookie();

        // then
        assertThat(cookieInHeader).contains(httpHeaders.getFirst(HttpHeaders.SET_COOKIE));
    }
}