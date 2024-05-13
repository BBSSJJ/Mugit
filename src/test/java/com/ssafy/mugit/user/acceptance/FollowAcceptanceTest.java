package com.ssafy.mugit.user.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.global.dto.ListDto;
import com.ssafy.mugit.user.dto.FollowerDto;
import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.FollowRepository;
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

import java.util.Arrays;
import java.util.List;

import static com.ssafy.mugit.fixure.ProfileFixture.*;
import static com.ssafy.mugit.fixure.UserFixture.*;
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

    User followingUser;
    User followeeUser;
    @Autowired
    private FollowRepository followRepository;

    @BeforeEach
    void setUp() {
        followingUser = USER.getFixture(PROFILE.getFixture());
        userRepository.save(followingUser);

        followeeUser = USER_2.getFixture(PROFILE_2.getFixture());
        userRepository.save(followeeUser);
    }

    @Test
    @DisplayName("[인수] 타인 팔로우 요청(201)")
    void testFollow() throws Exception {
        // given
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")).andReturn().getResponse().getCookies();

        // when
        ResultActions perform = mockMvc.perform(post("/api/users/" + followeeUser.getId() + "/follows")
                .cookie(loginCookie));

        // then
        perform.andExpect(status().is(201));
    }

    @Test
    @DisplayName("[인수] 본인 팔로우 요청(400)")
    void testFollowMyself() throws Exception {
        // given
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token"))
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
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token"))
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
        long followerId = followingUser.getId();
        long followeeId = followeeUser.getId();

        // me <-> user2
        followService.follow(followeeId, followerId);
        followService.follow(followerId, followeeId);

        // me -> user3
        User followee2 = USER_3.getFixture(PROFILE_3.getFixture());
        userRepository.save(followee2);
        followService.follow(followerId, followee2.getId());


        // when
        ResultActions perform = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token"));

        // then
        perform.andExpect(cookie().exists("followers"))
                .andExpect(cookie().exists("followings"))
                .andExpect(cookie().value("followers", "1"))
                .andExpect(cookie().value("followings", "2"))
                .andExpect(status().is(200));
    }

    @Test
    @DisplayName("[인수] 팔로우 / 팔로잉 유저 전체 조회(200)")
    void testAllFollowers() throws Exception {
        // given
        long myId = followingUser.getId();

        // me <-> followee
        followService.follow(followeeUser.getId(), myId);
        followService.follow(myId, followeeUser.getId());

        // me -> followee2
        User followee2 = USER_3.getFixture(PROFILE_3.getFixture());
        userRepository.save(followee2);
        followService.follow(myId, followee2.getId());

        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token"))
                .andReturn().getResponse().getCookies();

        List<FollowerDto> followers = Arrays.asList(new FollowerDto(followeeUser), new FollowerDto(followee2));
        String expectFollowersBody = objectMapper.writeValueAsString(new ListDto<>(followers));

        List<FollowerDto> followings = List.of(new FollowerDto(followeeUser));
        String expectFollowingsBody = objectMapper.writeValueAsString(new ListDto<>(followings));

        // when
        ResultActions followersResult = mockMvc.perform(get("/api/users/followers").cookie(loginCookie));
        ResultActions followingsResult = mockMvc.perform(get("/api/users/followings").cookie(loginCookie));

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
        long myId = followingUser.getId();
        long friend = followeeUser.getId();

        // me <-> friend
        followService.follow(friend, myId);
        followService.follow(myId, friend);

        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token"))
                .andReturn().getResponse().getCookies();


        // when
        ResultActions perform = mockMvc.perform(delete("/api/users/" + friend + "/follows")
                .cookie(loginCookie));
        // then
        perform.andExpect(status().isOk());
        assertThat(followRepository.countMyFollowings(myId)).isEqualTo(0);

        // when2
        ResultActions perform2 = mockMvc.perform(delete("/api/users/" + friend + "/follows")
                .cookie(loginCookie));

        //then 2
        perform2.andExpect(status().is(204));
    }

    @Test
    @DisplayName("[인수] 프로필에서 팔로우하는지 여부 조회(200)")
    void testFollowInProfile() throws Exception {
        // given
        long myId = followingUser.getId();

        // me <-> followee
        followService.follow(followeeUser.getId(), myId);
        followService.follow(myId, followeeUser.getId());

        // follower2 -> me
        User follower2 = USER_3.getFixture(PROFILE_3.getFixture());
        userRepository.save(follower2);
        followService.follow(follower2.getId(), myId);

        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token"))
                .andReturn().getResponse().getCookies();

        // when
        ResultActions followeeResult = mockMvc.perform(get("/api/users/" + followeeUser.getId() + "/profiles/detail").cookie(loginCookie));
        ResultActions follower2Result = mockMvc.perform(get("/api/users/" + follower2.getId() + "/profiles/detail").cookie(loginCookie));
        ResponseUserProfileDto followeeDto = objectMapper.readValue(followeeResult.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), ResponseUserProfileDto.class);
        ResponseUserProfileDto follower2Dto = objectMapper.readValue(follower2Result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), ResponseUserProfileDto.class);

        // then
        assertThat(followeeDto.getIsFollower()).isTrue();
        assertThat(followeeDto.getIsFollowing()).isTrue();
        assertThat(follower2Dto.getIsFollower()).isFalse();
        assertThat(follower2Dto.getIsFollowing()).isTrue();
    }
}
