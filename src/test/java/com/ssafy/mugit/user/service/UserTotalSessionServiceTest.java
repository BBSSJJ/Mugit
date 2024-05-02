package com.ssafy.mugit.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.user.dto.UserSessionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

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
        List<UserSessionDto> allLoginSessionIds = sut.findAllSession();
        for (UserSessionDto ret : allLoginSessionIds) {
            System.out.println("ret = " + ret);
        }

        // then
        assertThat(allLoginSessionIds).isNotNull();
    }
}