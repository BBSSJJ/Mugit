package com.ssafy.mugit.user.acceptance;

import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.global.web.GlobalExceptionHandler;
import com.ssafy.mugit.global.web.api.OAuthApi;
import com.ssafy.mugit.global.web.dto.MessageDto;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

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

    WebTestClient webClient;

    @BeforeEach
    void setup() {
        userLoginService = new UserLoginService(oAuthApi, userRepository, cookieUtil);
        userLoginController = new UserLoginController(userLoginService);
        webClient = WebTestClient
                .bindToController(userLoginController)
                .controllerAdvice(new GlobalExceptionHandler())
                .configureClient()
                .baseUrl("/api/users/")
                .build();

        // DB에 등록
        User user = UserFixture.DEFAULT_LOGIN_USER.getUser();
        user.regist(ProfileFixture.DEFAULT_PROFILE.getProfile());
        userRepository.save(user);
    }

    @Test
    @DisplayName("[인수] 정상 로그인 테스트")
    void testLoginSuccess() {
        // given
        String token = "qwerasdf1234";

        // when
        ResponseSpec result = webClient.get().uri("/login")
                .accept(MediaType.ALL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange();

        // then
        result.expectStatus().isOk()
                .expectHeader().valueMatches(HttpHeaders.SET_COOKIE, cookieUtil.getUserInfoCookie("isLogined", "true").toString())
                .expectBody(MessageDto.class).isEqualTo(new MessageDto("로그인 완료"));
    }

    @Test
    @DisplayName("[인수] 회원가입 필요 시 쿠키 설정된 Header 및 302 반환")
    void testNeedRegist() {
        // given
        String token = "not registered user token";
        
        // when
        ResponseSpec result = webClient.get().uri("/login")
                .accept(MediaType.ALL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange();

        // then
        result.expectStatus().isEqualTo(302)
                .expectHeader().valueMatches(HttpHeaders.SET_COOKIE, cookieUtil.getRegistCookie("needRegist", "true").toString())
                .expectBody(MessageDto.class).isEqualTo(new MessageDto("회원가입 필요"));
    }

    @Test
    @DisplayName("[인수] 인증 실패 시 401 반환")
    void testRegistFailed() {
        // given
        String token = "unauthorized google api token";

        // when
        ResponseSpec result = webClient.get().uri("/login")
                .accept(MediaType.ALL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange();

        // then
        result.expectStatus().isEqualTo(401)
                .expectBody(MessageDto.class).isEqualTo(new MessageDto("OAuth 인증 실패"));
    }
}
