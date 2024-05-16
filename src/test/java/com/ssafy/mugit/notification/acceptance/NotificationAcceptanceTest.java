package com.ssafy.mugit.notification.acceptance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.global.dto.ListDto;
import com.ssafy.mugit.global.dto.NotificationDto;
import com.ssafy.mugit.notification.entity.Notification;
import com.ssafy.mugit.notification.repository.NotificationRepository;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssafy.mugit.fixure.ProfileFixture.PROFILE;
import static com.ssafy.mugit.fixure.ProfileFixture.PROFILE_2;
import static com.ssafy.mugit.fixure.UserFixture.USER;
import static com.ssafy.mugit.fixure.UserFixture.USER_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AcceptanceTest
public class NotificationAcceptanceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    User me;
    User following;
    @Autowired
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        me = USER.getFixture(PROFILE.getFixture());
        following = USER_2.getFixture(PROFILE_2.getFixture());
        userRepository.save(me);
        userRepository.save(following);
    }

    @Test
    @DisplayName("[인수] 읽지 않은 알림 없을 시(404)")
    void testAllNotifications404() throws Exception {
        // given
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")).andReturn().getResponse().getCookies();

        // when
        ResultActions perform = mockMvc.perform(get("/api/users/notifications").cookie(loginCookie));

        // then
        perform.andExpect(status().is(404))
                .andExpect(content().json("{ \"message\" : \"안읽은 알림 없음\" }"));
    }

    @Test
    @DisplayName("[인수] 읽지 않은 알림 요청 시 전체응답(200)")
    void testAllNotifications200() throws Exception {
        // given
        Cookie[] followingUserLoginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token_2")).andReturn().getResponse().getCookies();
        Cookie[] myLoginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")).andReturn().getResponse().getCookies();

        // when
        // following -> user 팔로우
        mockMvc.perform(post("/api/users/" + me.getId() + "/follows").cookie(followingUserLoginCookie));

        // notifications 전체 조회
        byte[] responseBody = mockMvc.perform(get("/api/users/notifications").cookie(myLoginCookie))
                .andExpect(status().is(200)).andReturn().getResponse().getContentAsByteArray();
        ListDto<List<NotificationDto>> notificationDtos = objectMapper.readValue(responseBody, new TypeReference<ListDto<List<NotificationDto>>>() {});

        // then
        assertThat(notificationDtos.getList()).hasSize(1);
        assertThat(notificationDtos.getList().get(0).getDescription()).isEqualTo("leaf2님이 당신을 팔로우합니다.");
    }

    @Test
    @Transactional
    @DisplayName("[인수] 알림 읽기")
    void testReadNotification() throws Exception {
        // given
        Cookie[] followingUserLoginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token_2")).andReturn().getResponse().getCookies();
        Cookie[] myLoginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")).andReturn().getResponse().getCookies();

        // when 1
        ResultActions read404 = mockMvc.perform(patch("/api/users/notifications/-1").cookie(myLoginCookie));

        // then 1
        read404.andExpect(status().is(404))
                .andExpect(content().json("{ \"message\" : \"해당 알림 없음\" }"));

        // when 2
        mockMvc.perform(post("/api/users/" + me.getId() + "/follows").cookie(followingUserLoginCookie));
        long notificationId = notificationRepository.findAllReadableByUserId(me.getId()).get(0).getId();
        ResultActions read200 = mockMvc.perform(patch("/api/users/notifications/" + notificationId).cookie(myLoginCookie));
        Notification notification = notificationRepository.getReferenceById(notificationId);

        // then 2
        read200.andExpect(status().is(200))
                .andExpect(content().json("{ \"message\" : \"알림 읽기완료\" }"));
        assertThat(notification.getIsRead()).isTrue();
    }

    @Test
    @Transactional
    @DisplayName("[인수] 전체알림 읽기 성공(200)")
    void testReadAllNotification() throws Exception {
        // given
        Cookie[] followingUserLoginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token_2")).andReturn().getResponse().getCookies();
        mockMvc.perform(post("/api/users/" + me.getId() + "/follows").cookie(followingUserLoginCookie));
        Cookie[] myLoginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")).andReturn().getResponse().getCookies();

        // when
        ResultActions perform = mockMvc.perform(patch("/api/users/notifications").cookie(myLoginCookie));
        List<Notification> notification = notificationRepository.findAll();

        // then
        perform.andExpect(status().is(200))
                .andExpect(content().json("{ \"message\" : \"전체 알림 읽기완료\" }"));
        assertThat(notification).allMatch(Notification::getIsRead);
    }
}
