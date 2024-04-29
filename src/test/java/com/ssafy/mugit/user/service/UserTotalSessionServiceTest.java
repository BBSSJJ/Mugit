package com.ssafy.mugit.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.user.dto.SessionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTotalSessionServiceTest {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    UserTotalSessionService sut;

    @BeforeEach
    void setUp() {
        sut = new UserTotalSessionService(redisTemplate, new ObjectMapper());
    }

    @Test
    @DisplayName("[통합] redisTemplate으로 전체 세션 조회")
    void testFindTotalSession() throws JsonProcessingException {
        // given

        // when
        List<UserRedisDto> allLoginSessionIds = sut.findAllSession();
        for (UserRedisDto ret : allLoginSessionIds) {
            System.out.println("ret = " + ret);
        }

        // then
        assertThat(allLoginSessionIds).isNotNull();
    }

    @Test
    @DisplayName("[통합] 전체 세션 id 돌면서 회원정보 찾아오기")
    void testFindTotalSessionId() {
        // given

        // when
//        Set<Long> allLoginSessionIds = sut.findAllSessionId();

        // then
//        assertThat(allLoginSessionIds).isNull();
    }
}