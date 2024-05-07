package com.ssafy.mugit.user.acceptance;

import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.user.controller.UserLogoutController;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.fixture.ProfileFixture;
import com.ssafy.mugit.user.fixture.UserFixture;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.service.UserLogoutService;
import com.ssafy.mugit.user.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("logout")
@AcceptanceTest
public class UserLogoutAcceptanceTest {

    @Autowired
    CookieUtil cookieUtil;

    @Autowired
    UserLogoutService userLogoutService;

    @Autowired
    UserLogoutController userLogoutController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // DB에 등록
        User user = UserFixture.USER.getFixture();
        user.regist(ProfileFixture.PROFILE.getFixture());
        userRepository.save(user);
    }

    @Test
    @DisplayName("[인수] 로그인 후 로그아웃 시, 정상 응답 반환")
    void testLogoutSuccess() throws Exception {
        // given
        String token = "valid_token";
        mockMvc.perform(get("/api/users/login")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andDo(handler -> {

                    // when
                    ResultActions perform = mockMvc.perform(get("/api/users/logout")
                            .cookie(handler.getResponse().getCookies()));

                    // then
                    perform.andExpect(status().isOk())
                            .andExpect(content().json("{\"message\":\"로그아웃 완료\"}"))
                            .andDo(handler2 -> {
                                Cookie[] cookies2 = new Cookie[4];
                                for (Cookie cookie : handler2.getResponse().getCookies()) {
                                    if ("isLogined".equals(cookie.getName())) cookies2[0] = cookie;
                                    if ("nickName".equals(cookie.getName())) cookies2[1] = cookie;
                                    if ("profileText".equals(cookie.getName())) cookies2[2] = cookie;
                                    if ("profileImage".equals(cookie.getName())) cookies2[3] = cookie;
                                }
                                assertThat(cookies2[0].getValue()).isEmpty();
                                assertThat(cookies2[2].getValue()).isEmpty();
                                assertThat(cookies2[1].getValue()).isEmpty();
                                assertThat(cookies2[3].getValue()).isEmpty();
                            });
                });
    }

    @Test
    @DisplayName("[인수] 로그아웃 후 로그아웃 요청 시, 401 반환")
    void testLogoutTwice() throws Exception {
        // given
        String token = "valid_token";
        mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))

                // 로그인
                .andDo(loginHandler -> {
                    mockMvc.perform(get("/api/users/logout")
                                    .cookie(loginHandler.getResponse().getCookies()))

                            // 로그아웃
                            .andDo(logoutHandler -> {

                                // when
                                ResultActions perform = mockMvc.perform(get("/api/users/logout")
                                        .cookie(logoutHandler.getResponse().getCookies()));

                                // then
                                perform.andExpect(status().isUnauthorized());
                            });
                });
    }
}
