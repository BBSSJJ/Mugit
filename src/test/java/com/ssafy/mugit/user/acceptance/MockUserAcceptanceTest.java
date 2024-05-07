package com.ssafy.mugit.user.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.user.controller.MockUserController;
import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.fixture.MockUserInfoFixture;
import com.ssafy.mugit.user.fixture.ProfileFixture;
import com.ssafy.mugit.user.fixture.UserFixture;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.service.MockUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.ssafy.mugit.user.fixture.ProfileFixture.PROFILE;
import static com.ssafy.mugit.user.fixture.UserFixture.USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    @DisplayName("[인수] 테스트 회원가입 시 정상 응답(201)")
    void testRegistSuccess() throws Exception {
        // given
        String body = objectMapper.writeValueAsString(MockUserInfoFixture.MOCK_USER_INFO.getUserInfo());

        // when
        ResultActions perform = mockMvc.perform(post("/api/users/mocks/regist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(content().json("{\"message\":\"Mock 회원가입 완료\"}"));
    }

    @Test
    @DisplayName("[인수] pk 조회 후 테스트 로그인 시 정상 응답(200)")
    void testLoginSuccess() throws Exception {
        // given
        User user = USER.getFixture(PROFILE.getFixture());
        userRepository.save(user);
        String userDtoString = mockMvc.perform(get("/api/users/nick/" + user.getProfile().getNickName())).andReturn()
                .getResponse().getContentAsString();
        ResponseUserProfileDto userProfile = objectMapper.readValue(userDtoString, ResponseUserProfileDto.class);

        // when
        ResultActions perform = mockMvc.perform(get("/api/users/mocks/login")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pk", String.valueOf(userProfile.getId())));

        // then
        perform.andExpect(status().isOk())
                .andExpect(cookie().exists("isLogined"))
                .andExpect(content().json("{\"message\":\"Mock 로그인 완료\"}"));
    }
}
