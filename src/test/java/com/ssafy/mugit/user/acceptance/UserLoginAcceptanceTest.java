package com.ssafy.mugit.user.acceptance;

import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.global.web.api.OAuthApi;
import com.ssafy.mugit.user.controller.UserLoginController;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.fixture.ProfileFixture;
import com.ssafy.mugit.user.fixture.UserFixture;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.service.UserLoginService;
import com.ssafy.mugit.user.util.CookieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("login")
@AcceptanceTest
public class UserLoginAcceptanceTest {
    @Autowired
    @Qualifier("OAuthRestTemplateApi")
    OAuthApi oAuthApi;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CookieUtil cookieUtil;

    @Autowired
    UserLoginService userLoginService;

    @Autowired
    UserLoginController userLoginController;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // DB에 등록
        User user = UserFixture.DEFAULT_LOGIN_USER.getUser();
        user.regist(ProfileFixture.DEFAULT_PROFILE.getProfile());
        userRepository.save(user);
    }

    @Test
    @DisplayName("[인수] 정상 로그인 테스트")
    void testLoginSuccess() throws Exception {
        // given
        String token = "qwerasdf1234";

        // when
        ResultActions perform = mockMvc.perform(get("/api/users/login")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

        // then
        perform.andExpect(status().isOk())
                .andExpect(cookie().exists("isLogined"))
                .andExpect(cookie().exists("JSESSIONID"))
                .andExpect(content().json("{\"message\":\"로그인 완료\"}"));
    }

    @Test
    @DisplayName("[인수] 회원가입 필요 시 쿠키 설정된 Header 및 302 반환")
    void testNeedRegist() throws Exception {
        // given
        String token = "not registered user token";

        // when
        ResultActions perform = mockMvc.perform(get("/api/users/login")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

        // then
        perform.andExpect(status().is(302))
                .andExpect(cookie().exists("needRegist"))
                .andExpect(content().json("{\"message\":\"회원가입 필요\"}"));
    }

    @Test
    @DisplayName("[인수] 인증 실패 시 401 반환")
    void testRegistFailed() throws Exception {
        // given
        String token = "unauthorized google api token";

        // when
        ResultActions perform = mockMvc.perform(get("/api/users/login")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

        // then
        perform.andExpect(status().is(401))
                .andExpect(content().json("{\"message\":\"OAuth 인증 실패\"}"));
    }
}
