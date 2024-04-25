package com.ssafy.mugit.user.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.user.controller.UserProfileController;
import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.fixture.ProfileFixture;
import com.ssafy.mugit.user.fixture.UserFixture;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.service.UserProfileService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Tag("profile")
@AcceptanceTest
public class UserProfileAcceptanceTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    UserProfileController userProfileController;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = UserFixture.DEFAULT_LOGIN_USER.getUser();
        Profile profile = ProfileFixture.DEFAULT_PROFILE.getProfile();
        user.regist(profile);
        userRepository.save(user);
    }

    @Test
    @DisplayName("[인수] 로그인 안한 유저 타인 프로필 조회(200)")
    void testFindUserPk() throws Exception {
        // given
        String resultJson = objectMapper.writeValueAsString(
                new ResponseUserProfileDto(
                        UserFixture.DEFAULT_LOGIN_USER.getUser(),
                        ProfileFixture.DEFAULT_PROFILE.getProfile()));
        long userId = 1L;

        // when
        ResultActions perform = mockMvc.perform(get("/api/users/" + userId + "/profiles/detail")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(content().json(resultJson));
    }

    @Test
    @DisplayName("[인수] 로그인한 유저 본인 프로필 조회(200)")
    void testFindMyProfile() throws Exception {
        // given
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login")
                .header(HttpHeaders.AUTHORIZATION, "Bearer qwerasdf1234"))
                .andReturn().getResponse().getCookies();
        String resultJson = objectMapper.writeValueAsString(
                new ResponseUserProfileDto(
                        UserFixture.DEFAULT_LOGIN_USER.getUser(),
                        ProfileFixture.DEFAULT_PROFILE.getProfile()));

        // when
        ResultActions perform = mockMvc.perform(get("/api/users/profiles/detail")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(loginCookie));

        // then
        perform.andExpect(status().isOk())
                .andExpect(content().json(resultJson));
    }

    @Test
    @DisplayName("[인수] 로그인 안한 유저 본인 프로필 조회(403)")
    void testFindMyProfileWithoutLogin() throws Exception {
        // given
        // when
        ResultActions perform = mockMvc.perform(get("/api/users/profiles/detail")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isUnauthorized());
    }

}
