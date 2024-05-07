package com.ssafy.mugit.user.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.user.controller.UserDeleteController;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.fixture.ProfileFixture;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.service.UserDeleteService;
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

import static com.ssafy.mugit.user.fixture.UserFixture.USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("delete")
@AcceptanceTest
public class UserDeleteAcceptanceTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    UserDeleteService userDeleteService;
    
    @Autowired
    UserDeleteController userDeleteController;

    @BeforeEach
    void setUp() {
        User user = USER.getFixture();
        Profile profile = ProfileFixture.PROFILE.getFixture();
        user.regist(profile);
        userRepository.save(user);
    }
    
    @Test
    @DisplayName("[인수] 회원탈퇴 시 해당유저, 프로필 정상 삭제 확인(200)")
    void testDeleteUser() throws Exception {
        // given
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token"))
                .andReturn().getResponse().getCookies();
        
        // when
        ResultActions perform = mockMvc.perform(delete("/api/users")
                        .cookie(loginCookie).contentType(MediaType.APPLICATION_JSON));
        
        // then
        perform.andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"회원탈퇴 완료\"}"));

    }

    @Test
    @DisplayName("[인수] 로그인 없이 회원탈퇴 시 인증오류(401)")
    void testDeleteUserWithoutLogin() throws Exception {
        // given

        // when
        ResultActions perform = mockMvc.perform(delete("/api/users")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isUnauthorized());
    }
}
