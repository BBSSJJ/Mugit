package com.ssafy.mugit.user.acceptance;

import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.global.web.GlobalExceptionHandler;
import com.ssafy.mugit.global.web.dto.MessageDto;
import com.ssafy.mugit.user.controller.UserRegistController;
import com.ssafy.mugit.user.dto.request.RegistProfileDto;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.fixture.ProfileFixture;
import com.ssafy.mugit.user.fixture.RegistProfileDtoFixture;
import com.ssafy.mugit.user.fixture.UserFixture;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.service.UserRegistService;
import com.ssafy.mugit.user.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

@Tag("regist")
@Slf4j
@AcceptanceTest
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
    @DisplayName("[인수] 정상 회원가입 시 쿠키 설정된 Header 및 201 반환")
    void testTempUserRegist() throws Exception {
        // given
        String needRegistCookie = "true";
        String snsIdCookie = "asdf1234";
        String snsTypeCookie = "GOOGLE";
        String emailCookie = "test@test.com";
        RegistProfileDto registProfileDto = RegistProfileDtoFixture.DEFAULT_REGIST_PROFILE_DTO.getRegistProfileDto();

        // when
        ResponseSpec result = webClient.post().uri("/profiles")
                .accept(MediaType.ALL)
                .cookie("needRegist", needRegistCookie)
                .cookie("snsId", snsIdCookie)
                .cookie("snsType", snsTypeCookie)
                .cookie("email", emailCookie)
                .bodyValue(registProfileDto).exchange();

        // then
        result.expectStatus().isCreated()
                .expectHeader().valueMatches(HttpHeaders.SET_COOKIE, cookieUtil.getUserInfoCookie("isLogined", "true").toString())
                .expectHeader().values(HttpHeaders.SET_COOKIE, cookie -> log.info(cookie.toString()))
                .expectBody(MessageDto.class).isEqualTo(new MessageDto("회원가입 완료"));
    }

    @Test
    @DisplayName("[인수] 중복 프로필 시 409 반환")
    void testDuplicationNickName() throws Exception {
        // given
        User tempUser = UserFixture.DEFAULT_LOGIN_USER_2.getUser();
        tempUser.regist(ProfileFixture.DEFAULT_PROFILE.getProfile());
        userRepository.save(tempUser);
        String needRegistCookie = "true";
        String snsIdCookie = "asdf1234";
        String snsTypeCookie = "GOOGLE";
        String emailCookie = "test@test.com";
        RegistProfileDto registProfileDto = new RegistProfileDto("leaf", "프로필", "profile/image/path");

        // when
        ResponseSpec result = webClient.post().uri("/profiles")
                .accept(MediaType.ALL)
                .cookie("needRegist", needRegistCookie)
                .cookie("snsId", snsIdCookie)
                .cookie("snsType", snsTypeCookie)
                .cookie("email", emailCookie)
                .bodyValue(registProfileDto).exchange();

        // then
        result.expectStatus().isEqualTo(409)
                .expectBody(MessageDto.class).isEqualTo(new MessageDto("중복 닉네임"));

    }

}
