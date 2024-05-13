package com.ssafy.mugit.user.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.user.controller.UserProfileController;
import com.ssafy.mugit.user.dto.request.RequestModifyUserInfoDto;
import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.ProfileRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.service.UserProfileService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.ssafy.mugit.fixure.ModifyUserInfoFixture.MODIFY_USER_INFO_DTO_01;
import static com.ssafy.mugit.fixure.ProfileFixture.PROFILE;
import static com.ssafy.mugit.fixure.ProfileFixture.PROFILE_2;
import static com.ssafy.mugit.fixure.UserFixture.USER;
import static com.ssafy.mugit.fixure.UserFixture.USER_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    @Autowired
    ProfileRepository profileRepository;

    User userInDB;

    @BeforeEach
    void setUp() {
        userInDB = USER.getFixture(PROFILE.getFixture());
        userRepository.save(userInDB);
    }

    @Test
    @DisplayName("[인수] 로그인 안한 유저 타인 프로필 조회(200)")
    void testFindUserPk() throws Exception {
        // given
        ResponseUserProfileDto value = new ResponseUserProfileDto(userInDB, userInDB.getProfile());
        value.setIsMyProfile(false);
        value.setFollowCount(0, 0);
        String resultJson = objectMapper.writeValueAsString(value);
        long userId = userInDB.getId();

        // when
        ResultActions perform = mockMvc.perform(get("/api/users/" + userId + "/profiles/detail").contentType(APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(content().json(resultJson));
    }

    @Test
    @DisplayName("[인수] 로그인한 유저 본인 프로필 조회(200)")
    void testFindMyProfile() throws Exception {
        // given
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")).andReturn().getResponse().getCookies();
        ResponseUserProfileDto value = new ResponseUserProfileDto(userInDB, userInDB.getProfile());
        value.setIsMyProfile(false);
        value.setFollowCount(0, 0);
        String resultJson = objectMapper.writeValueAsString(value);

        // when
        ResultActions perform = mockMvc.perform(get("/api/users/profiles/detail").contentType(APPLICATION_JSON)
                .cookie(loginCookie));

        // then
        perform.andExpect(status().isOk())
                .andExpect(content().json(resultJson));
    }

    @Test
    @DisplayName("[인수] 로그인 안한 유저 본인 프로필 조회(401)")
    void testFindMyProfileWithoutLogin() throws Exception {
        // given
        // when
        ResultActions perform = mockMvc.perform(get("/api/users/profiles/detail").contentType(APPLICATION_JSON));

        // then
        perform.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[인수] 본인 회원정보 정상 수정(200)")
    void testModifyMyProfile() throws Exception {
        // given
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")).andReturn().getResponse().getCookies();
        RequestModifyUserInfoDto dto = MODIFY_USER_INFO_DTO_01.getFixture();
        String body = objectMapper.writeValueAsString(dto);

        // when
        ResultActions perform = mockMvc.perform(patch("/api/users/profiles")
                .cookie(loginCookie)
                .contentType(APPLICATION_JSON).content(body));

        // then
        ResponseUserProfileDto userProfile = userRepository.findUserProfileDtoByUserId(userInDB.getId());
        assertThat(userProfile.getNickName()).isEqualTo(dto.getNickName());
        assertThat(userProfile.getProfileText()).isEqualTo(dto.getProfileText());
        assertThat(userProfile.getProfileImagePath()).isEqualTo(dto.getProfileImagePath());

        perform.andExpect(status().isOk())
                .andExpect(cookie().value("nickName", URLEncoder.encode(userProfile.getNickName(), StandardCharsets.UTF_8)))
                .andExpect(cookie().value("profileText", URLEncoder.encode(userProfile.getProfileText(), StandardCharsets.UTF_8)))
                .andExpect(cookie().value("profileImagePath", URLEncoder.encode(userProfile.getProfileImagePath(), StandardCharsets.UTF_8)))
                .andExpect(content().json("{\"message\":\"프로필 수정완료\"}"));

    }

    @Test
    @DisplayName("[인수] 로그인 없이 회원정보 수정(401)")
    void testModifyMyProfileWithoutLogin() throws Exception {
        // given
        RequestModifyUserInfoDto dto = MODIFY_USER_INFO_DTO_01.getFixture();
        String body = objectMapper.writeValueAsString(dto);

        // when
        ResultActions perform = mockMvc.perform(patch("/api/users/profiles")
                .contentType(APPLICATION_JSON)
                .content(body));

        // then
        perform.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[인수] 중복 프로필 수정(409)")
    void testModifyDuplicateProfile() throws Exception {
        // given
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")).andReturn().getResponse().getCookies();
        String body = objectMapper.writeValueAsString(MODIFY_USER_INFO_DTO_01.getFixture());
        userRepository.save(USER_2.getFixture(PROFILE_2.getFixture()));

        // when
        ResultActions perform = mockMvc.perform(patch("/api/users/profiles").cookie(loginCookie).contentType(APPLICATION_JSON).content(body));

        // then
        perform.andExpect(status().is(409))
                .andExpect(content().json("{\"message\":\"중복 닉네임\"}"));
    }
}
