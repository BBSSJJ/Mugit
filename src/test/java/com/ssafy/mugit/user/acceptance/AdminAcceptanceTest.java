package com.ssafy.mugit.user.acceptance;

import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.user.controller.AdminController;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.ssafy.mugit.user.fixture.ProfileFixture.DEFAULT_PROFILE;
import static com.ssafy.mugit.user.fixture.UserFixture.DEFAULT_LOGIN_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("admin")
@AcceptanceTest
public class AdminAcceptanceTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AdminController adminController;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("[인수] 관리자 로그인 시 관리자 권한 부여(200)")
    void testAdminLogin() throws Exception {
        // given
        String id = "mugit";
        String password = "Mugit502!";

        // when
        ResultActions perform = mockMvc.perform(post("/login")
                .param("username", id)
                .param("password", password));

        // then
        perform.andExpect(status().isOk());
    }

    @Test
    @DisplayName("[인수] 관리자 로그인 실패 시 인증 오류(401)")
    void testAdminLoginFail() throws Exception {
        // given
        String id = "failId";
        String password = "failPassword";

        // when
        ResultActions perform = mockMvc.perform(post("/login")
                .param("username", id)
                .param("password", password));

        // then
        perform.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[인수] 관리자 권한일때 전체 세션조회기능 사용 가능")
    void testTotalSessionGet() throws Exception {
        // given
        Cookie[] adminCookie = mockMvc.perform(post("/login")
                        .param("username", "mugit")
                        .param("password", "Mugit502!"))
                .andReturn().getResponse().getCookies();

        // when
        ResultActions perform = mockMvc.perform(get("/api/admin/sessions")
                .cookie(adminCookie));

        // then
        perform.andExpect(status().isOk());
    }

    @Test
    @DisplayName("[인수] 관리자 권한아닐때 전체 세션조회기능 사용 불가능")
    void testTotalSessionGetWithoutAdmin() throws Exception {
        // given
        User user = DEFAULT_LOGIN_USER.getFixture();
        user.regist(DEFAULT_PROFILE.getFixture());
        userRepository.save(user);

        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer qwerasdf1234"))
                .andReturn().getResponse().getCookies();

        // when
        ResultActions perform = mockMvc.perform(get("/api/admin/sessions")
                .cookie(loginCookie));

        // then
        perform.andExpect(status().is(403));
    }

    @Test
    @DisplayName("[인수] 로그인 안했을때 전체 세션조회기능 사용 불가능")
    void testTotalSessionGetWithoutLogin() throws Exception {
        // given

        // when
        ResultActions perform = mockMvc.perform(get("/api/admin/sessions"));

        // then
        perform.andExpect(status().isUnauthorized());
    }
}
