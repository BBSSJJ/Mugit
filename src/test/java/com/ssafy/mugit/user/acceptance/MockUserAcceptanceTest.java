package com.ssafy.mugit.user.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.user.controller.MockUserController;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.fixture.MockUserInfoFixture;
import com.ssafy.mugit.user.fixture.ProfileFixture;
import com.ssafy.mugit.user.fixture.UserFixture;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.service.MockUserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("mock")
@AcceptanceTest
public class MockUserAcceptanceTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockUserService mockUserService;

    @Autowired
    MockUserController userController;

    @Autowired
    UserRepository userRepository;

    Cookie[] adminCookies;

    /**
     * 차후 관리자 기능 도입시 쿠키 획득용
    @BeforeEach
    void setUp() throws Exception {
        adminCookies = mockMvc.perform(post("/login")
                        .param("username", "mugit")
                        .param("password", "Mugit502!"))
                .andReturn().getResponse().getCookies();
    }
     */

    @Test
    @DisplayName("[인수] 테스트 회원가입 시 정상 응답(201)")
    void testRegistSuccess() throws Exception {
        // given
        String body = objectMapper.writeValueAsString(MockUserInfoFixture.MOCK_USER_INFO.getUserInfo());

        // when
        ResultActions perform = mockMvc.perform(post("/api/users/mocks/regist")
                .cookie(adminCookies)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(content().json("{\"message\":\"Mock 회원가입 완료\"}"));
    }

    @Test
    @DisplayName("[인수] 테스트 로그인 시 정상 응답(200)")
    void testLoginSuccess() throws Exception {
        // given
        User user = UserFixture.DEFAULT_LOGIN_USER.getUser();
        user.regist(ProfileFixture.DEFAULT_PROFILE.getProfile());
        userRepository.save(user);


        // when
        ResultActions perform = mockMvc.perform(post("/api/users/mocks/login")
                .cookie(adminCookies)
                .contentType(MediaType.APPLICATION_JSON)
                .param("pk", "1"));

        // then
        perform.andExpect(status().isOk())
                .andExpect(cookie().exists("isLogined"))
                .andExpect(content().json("{\"message\":\"Mock 로그인 완료\"}"));
    }
}
