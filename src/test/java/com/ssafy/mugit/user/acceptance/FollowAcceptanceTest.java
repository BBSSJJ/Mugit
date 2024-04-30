package com.ssafy.mugit.user.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.global.web.dto.ListDto;
import com.ssafy.mugit.user.dto.FollowerDto;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.service.FollowService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.ssafy.mugit.user.fixture.FollowerFixture.FOLLOWER_USER_2;
import static com.ssafy.mugit.user.fixture.FollowerFixture.FOLLOWER_USER_3;
import static com.ssafy.mugit.user.fixture.ProfileFixture.*;
import static com.ssafy.mugit.user.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("follow")
@AcceptanceTest
public class FollowAcceptanceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowService followService;
    @Qualifier("objectMapper")
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        User user = USER.getFixture();
        user.regist(PROFILE.getFixture());
        userRepository.save(user);

        User user2 = USER_2.getFixture();
        user2.regist(PROFILE_2.getFixture());
        userRepository.save(user2);
    }

    @Test
    @DisplayName("[인수] 타인 팔로우 요청(201)")
    void testFollow() throws Exception {
        // given
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer qwerasdf1234"))
                .andReturn().getResponse().getCookies();

        Long followeeId = USER_2.getFixture().getId();

        // when
        ResultActions perform = mockMvc.perform(post("/api/users/" + followeeId + "/follows")
                .cookie(loginCookie));

        // then
        perform.andExpect(status().is(201));
    }

    @Test
    @DisplayName("[인수] 본인 팔로우 요청(400)")
    void testFollowMyself() throws Exception {
        // given
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer qwerasdf1234"))
                .andReturn().getResponse().getCookies();

        Long followeeId = USER.getFixture().getId();

        // when
        ResultActions perform = mockMvc.perform(post("/api/users/" + followeeId + "/follows")
                .cookie(loginCookie));

        // then
        perform.andExpect(status().is(400));
    }

    @Test
    @DisplayName("[인수] 없는 유저 팔로우 요청(404)")
    void testFollowNotInDB() throws Exception {
        // given
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer qwerasdf1234"))
                .andReturn().getResponse().getCookies();
        long followingId = -1L;

        // when
        ResultActions perform = mockMvc.perform(post("/api/users/" + followingId + "/follows")
                .cookie(loginCookie));

        // then
        perform.andExpect(status().is(404));
    }

    @Test
    @DisplayName("[인수] 로그인 시 팔로우, 팔로잉 유저 수 조회(200, login)")
    void testLoginFollow() throws Exception {
        // given
        long myId = USER.getFixture().getId();

        // me <-> user2
        User user2 = USER_2.getFixture(PROFILE_2.getFixture());
        followService.follow(user2.getId(), myId);
        followService.follow(myId, user2.getId());

        // me -> user3
        User user3 = USER_3.getFixture(PROFILE_3.getFixture());
        userRepository.save(user3);
        followService.follow(myId, user3.getId());


        // when
        ResultActions perform = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer qwerasdf1234"));

        // then
        perform.andExpect(cookie().exists("followers"))
                .andExpect(cookie().exists("followings"))
                .andExpect(cookie().value("followers", "2"))
                .andExpect(cookie().value("followings", "1"))
                .andExpect(status().is(200));
    }

    @Test
    @DisplayName("[인수] 팔로우 / 팔로잉 유저 전체 조회(200)")
    void testAllFollowers() throws Exception {
        // given
        long myId = USER.getFixture().getId();

        // me <-> user2
        User user2 = USER_2.getFixture(PROFILE_2.getFixture());
        followService.follow(user2.getId(), myId);
        followService.follow(myId, user2.getId());

        // me -> user3
        User user3 = USER_3.getFixture(PROFILE_3.getFixture());
        userRepository.save(user3);
        followService.follow(myId, user3.getId());

        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer qwerasdf1234"))
                .andReturn().getResponse().getCookies();

        List<FollowerDto> followers = new ArrayList<>();
        followers.add(FOLLOWER_USER_2.getFixture());
        followers.add(FOLLOWER_USER_3.getFixture());
        String expectFollowersBody = objectMapper.writeValueAsString(new ListDto<>(followers));

        List<FollowerDto> followings = new ArrayList<>();
        followings.add(FOLLOWER_USER_2.getFixture());
        String expectFollowingsBody = objectMapper.writeValueAsString(new ListDto<>(followings));

        // when
        ResultActions followersResult = mockMvc.perform(get("/api/users/followers")
                .cookie(loginCookie));
        ResultActions followingsResult = mockMvc.perform(get("/api/users/followings")
                .cookie(loginCookie));

        // then
        followersResult.andExpect(status().isOk())
                .andExpect(content().json(expectFollowersBody));
        followingsResult.andExpect(status().isOk())
                .andExpect(content().json(expectFollowingsBody));
    }

    @Test
    @DisplayName("[인수] 팔로우 정상삭제(200)")
    void testFollowDelete() throws Exception {
        // given
        long myId = USER.getFixture().getId();
        long followerId = USER_2.getFixture().getId();

        // me <-> user2
        followService.follow(followerId, myId);
        followService.follow(myId, followerId);

        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer qwerasdf1234"))
                .andReturn().getResponse().getCookies();


        // when
        ResultActions perform = mockMvc.perform(delete("/api/users/" + followerId + "/follows")
                .cookie(loginCookie));
        // then
        perform.andExpect(status().isOk());
        assertThat(followService.getAllFollowerCount(myId)).isEqualTo(0);

        // when2
        ResultActions perform2 = mockMvc.perform(delete("/api/users/" + followerId + "/follows")
                .cookie(loginCookie));

        //then 2
        perform2.andExpect(status().is(204));
    }
}
