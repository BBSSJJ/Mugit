package com.ssafy.mugit.user.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.user.controller.UserProfileController;
import com.ssafy.mugit.user.dto.request.RequestModifyUserInfoDto;
import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.fixture.ProfileFixture;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.ssafy.mugit.user.fixture.ModifyUserInfoFixture.DEFAULT_MODIFY_USER_INFO_DTO;
import static com.ssafy.mugit.user.fixture.ModifyUserInfoFixture.DUPLICATE_MODIFY_USER_INFO_DTO;
import static com.ssafy.mugit.user.fixture.UserFixture.DEFAULT_LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
    @Autowired
    private ProfileRepository profileRepository;

    @BeforeEach
    void setUp() {
        User user = DEFAULT_LOGIN_USER.getFixture();
        Profile profile = ProfileFixture.DEFAULT_PROFILE.getFixture();
        user.regist(profile);
        userRepository.save(user);
    }

    @Test
    @DisplayName("[인수] 로그인 안한 유저 타인 프로필 조회(200)")
    void testFindUserPk() throws Exception {
        // given
        String resultJson = objectMapper.writeValueAsString(
                new ResponseUserProfileDto(
                        DEFAULT_LOGIN_USER.getFixture(),
                        ProfileFixture.DEFAULT_PROFILE.getFixture()));
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
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer qwerasdf1234"))
                .andReturn().getResponse().getCookies();
        String resultJson = objectMapper.writeValueAsString(
                new ResponseUserProfileDto(
                        DEFAULT_LOGIN_USER.getFixture(),
                        ProfileFixture.DEFAULT_PROFILE.getFixture()));

        // when
        ResultActions perform = mockMvc.perform(get("/api/users/profiles/detail")
                .contentType(MediaType.APPLICATION_JSON)
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
        ResultActions perform = mockMvc.perform(get("/api/users/profiles/detail")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[인수] 본인 회원정보 정상 수정(200)")
    void testModifyMyProfile() throws Exception {
        // given
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer qwerasdf1234"))
                .andReturn().getResponse().getCookies();
        RequestModifyUserInfoDto dto = DEFAULT_MODIFY_USER_INFO_DTO.getFixture();
        String body = objectMapper.writeValueAsString(dto);

        // when
        ResultActions perform = mockMvc.perform(patch("/api/users/profiles")
                .cookie(loginCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
        Profile profileInDB = profileRepository.findByUserId(DEFAULT_LOGIN_USER.getFixture().getId());

        // then
        perform.andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"프로필 수정완료\"}"));
        assertThat(profileInDB.getNickName()).isEqualTo(dto.getNickName());
        assertThat(profileInDB.getProfileText()).isEqualTo(dto.getProfileText());
        assertThat(profileInDB.getProfileImagePath()).isEqualTo(dto.getProfileImagePath());
    }

    @Test
    @DisplayName("[인수] 로그인 없이 회원정보 수정(401)")
    void testModifyMyProfileWithoutLogin() throws Exception {
        // given
        RequestModifyUserInfoDto dto = DEFAULT_MODIFY_USER_INFO_DTO.getFixture();
        String body = objectMapper.writeValueAsString(dto);

        // when
        ResultActions perform = mockMvc.perform(patch("/api/users/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        perform.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[인수] 중복 프로필 수정(409)")
    void testModifyDuplicateProfile() throws Exception {
        // given
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer qwerasdf1234"))
                .andReturn().getResponse().getCookies();
        RequestModifyUserInfoDto dto = DUPLICATE_MODIFY_USER_INFO_DTO.getFixture();
        String body = objectMapper.writeValueAsString(dto);

        // when
        ResultActions perform = mockMvc.perform(patch("/api/users/profiles")
                .cookie(loginCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
        Profile profileInDB = profileRepository.findByUserId(DEFAULT_LOGIN_USER.getFixture().getId());

        // then
        perform.andExpect(status().is(409))
                .andExpect(content().json("{\"message\":\"중복 닉네임\"}"));
    }
}
