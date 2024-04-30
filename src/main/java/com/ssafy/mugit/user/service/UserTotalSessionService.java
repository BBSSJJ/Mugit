package com.ssafy.mugit.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.user.dto.UserSessionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ssafy.mugit.auth.SessionKeys.LOGIN_USER_KEY;

@Service
@RequiredArgsConstructor
public class UserTotalSessionService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public List<UserSessionDto> findAllSession() {

        Cursor<String> scan = redisTemplate.scan(ScanOptions.scanOptions().match("*").count(10).build());
        List<UserSessionDto> sessions = new ArrayList<>();
        while (scan.hasNext()) {
            String key = scan.next();
            Object user = redisTemplate.opsForHash().get(key, LOGIN_USER_KEY.getSessionKey());
            if (user != null) {
                UserSessionDto userDto = objectMapper.convertValue(user, UserSessionDto.class);
                sessions.add(userDto);
            }
        }
        return sessions;
    }
}
