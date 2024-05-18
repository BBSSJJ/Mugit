package com.ssafy.mugit.user.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.fixure.ProfileFixture;
import com.ssafy.mugit.fixure.RegistProfileDtoFixture;
import com.ssafy.mugit.fixure.UserFixture;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.service.UserRegistService;
import com.ssafy.mugit.user.util.UserCookieUtil;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("regist")
@AcceptanceTest
public class UserRegistAcceptanceTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRegistService userRegistService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCookieUtil userCookieUtil;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        userRepository.save(UserFixture.USER.getFixture());
    }

    @Test
    @DisplayName("[인수] 정상 회원가입 시 쿠키 설정된 Header 및 201 반환")
    void testTempUserRegist() throws Exception {
        // given
        Cookie[] cookies = new Cookie[4];
        cookies[0] = new Cookie("needRegist","true");
        cookies[1] = new Cookie("snsId","asdf12345");
        cookies[2] = new Cookie("snsType","GOOGLE");
        cookies[3] = new Cookie("email","test@test.com");
        String body = objectMapper.writeValueAsString(RegistProfileDtoFixture.DEFAULT_REGIST_PROFILE_DTO.getRegistProfileDto());

        // when
        ResultActions perform = mockMvc.perform(post("/api/users/regist")
                .cookie(cookies)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(cookie().exists("isLogined"))
                .andExpect(cookie().exists("JSESSIONID"))
                .andExpect(content().json("{\"message\":\"회원가입 완료\"}"));
    }

    @Test
    @DisplayName("[인수] 중복 프로필 시 409 반환")
    void testDuplicationNickName() throws Exception {
        // given
        User tempUser = UserFixture.USER_2.getFixture();
        tempUser.regist(ProfileFixture.PROFILE.getFixture());
        userRepository.save(tempUser);
        Cookie[] cookies = new Cookie[4];
        cookies[0] = new Cookie("needRegist","true");
        cookies[1] = new Cookie("snsId","asdf1234");
        cookies[2] = new Cookie("snsType","GOOGLE");
        cookies[3] = new Cookie("email","test@test.com");
        String body = objectMapper.writeValueAsString(RegistProfileDtoFixture.DEFAULT_REGIST_PROFILE_DTO.getRegistProfileDto());

        // when
        ResultActions perform = mockMvc.perform(post("/api/users/regist")
                .cookie(cookies)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        perform.andExpect(status().is(409))
                .andExpect(content().json("{\"message\":\"중복 닉네임\"}"));
    }

}
