package com.ssafy.mugit.user.acceptance;

import com.ssafy.mugit.user.util.CookieUtil;
import com.ssafy.mugit.global.web.dto.MessageDto;
import com.ssafy.mugit.global.web.GlobalExceptionHandler;
import com.ssafy.mugit.user.fixture.ProfileFixture;
import com.ssafy.mugit.user.fixture.UserFixture;
import com.ssafy.mugit.user.controller.UserRegistController;
import com.ssafy.mugit.user.dto.request.RegistProfileDto;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.service.UserRegistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRegistAcceptanceTest {

    @Autowired
    UserRegistService userRegistService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CookieUtil cookieUtil;

    WebTestClient webClient;

    @BeforeEach
    void setup() {
        webClient = WebTestClient
                .bindToController(new UserRegistController(userRegistService))
                .controllerAdvice(new GlobalExceptionHandler())
                .configureClient()
                .baseUrl("/api/users/")
                .build();

        userRepository.save(UserFixture.DEFAULT_LOGIN_USER.getUser());
    }

    @Test
    @DisplayName("임시 유저 정상 회원가입 테스트")
    void testTempUserRegist() throws Exception {
        // given
        String snsIdCookie = "asdf1234";
        String snsTypeCookie = "GOOGLE";
        RegistProfileDto registProfileDto = new RegistProfileDto("leaf", "프로필", "profile/image/path");

        // when
        ResponseSpec result = webClient.post().uri("/profiles")
                .accept(MediaType.ALL)
                .cookie("snsId", snsIdCookie)
                .cookie("snsType", snsTypeCookie)
                .bodyValue(registProfileDto).exchange();

        // then
        result.expectStatus().isCreated()
                .expectHeader().valueMatches(HttpHeaders.SET_COOKIE, cookieUtil.getUserInfoCookie("isLogined", "true").toString())
                .expectBody(MessageDto.class).isEqualTo(new MessageDto("회원가입 완료"));
    }

    @Test
    @DisplayName("쿠키 정상 발급 테스트")
    void testGetCookie() {
        // given
        String snsIdCookie = "asdf1234";
        SnsType snsTypeCookie = SnsType.GOOGLE;
        RegistProfileDto registProfileDto = new RegistProfileDto("leaf", "프로필", "profile/image/path");

        // when
        List<String> cookies = userRegistService.registAndGetLoginCookieHeader(snsIdCookie, snsTypeCookie, registProfileDto).get(HttpHeaders.SET_COOKIE);

        // then
        assertThat(cookies).contains(cookieUtil.getUserInfoCookie("isLogined", "true").toString());
        assert cookies != null;
        assertThat(cookies).anyMatch(cookie -> cookie.contains("leaf"))
                .anyMatch(cookie -> cookie.contains("%ED%94%84%EB%A1%9C%ED%95%84"))
                .anyMatch(cookie -> cookie.contains("profile%2Fimage%2Fpath"));
    }

    @Test
    @DisplayName("회원가입 시 닉네임 중복검사 하는지 테스트")
    void testDuplicationNickName() throws Exception {
        // given
        User tempUser = UserFixture.DEFAULT_LOGIN_USER_2.getUser(ProfileFixture.DEFAULT_PROFILE.getProfile());
        userRepository.save(tempUser);
        String snsIdCookie = "qwer1234";
        String snsTypeCookie = "GOOGLE";
        RegistProfileDto registProfileDto = new RegistProfileDto("leaf", "프로필", "profile/image/path");

        // when
        try {
            ResponseSpec result = webClient.post().uri("/profiles")
                    .accept(MediaType.ALL)
                    .cookie("snsId", snsIdCookie)
                    .cookie("snsType", snsTypeCookie)
                    .bodyValue(registProfileDto).exchange();

            // then
            result.expectStatus().isEqualTo(409)
                    .expectBody(MessageDto.class).isEqualTo(new MessageDto("중복 닉네임"));

            // remove temp user
        } finally {
            userRepository.delete(tempUser);
        }
    }

}
