package com.ssafy.mugit.user.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.user.controller.MockUserController;
import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.fixure.ProfileFixture;
import com.ssafy.mugit.fixure.UserFixture;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.service.MockUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AcceptanceTest
public class UserFindServiceAcceptanceTest {
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
    @DisplayName("[인수] 닉네임으로 회원찾기 시 정상 응답(200)")
    void testFindUserPk() throws Exception {
        // given
        User user = UserFixture.USER.getFixture();
        Profile profile = ProfileFixture.PROFILE.getFixture();
        user.regist(profile);
        userRepository.save(user);
        String resultJson = objectMapper.writeValueAsString(new ResponseUserProfileDto(user, profile));
        String nick = "leaf";

        // when
        ResultActions perform = mockMvc.perform(get("/api/users/nick/" + nick)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(content().json(resultJson));
    }
}
