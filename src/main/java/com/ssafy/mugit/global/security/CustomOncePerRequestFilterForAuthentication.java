package com.ssafy.mugit.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.dto.UserSessionDto;
import com.ssafy.mugit.user.entity.type.RoleType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

import static com.ssafy.mugit.global.dto.DataKeys.LOGIN_USER_KEY;
import static java.util.Objects.isNull;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOncePerRequestFilterForAuthentication extends OncePerRequestFilter {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader("Authorization") == null || request.getHeader("Authorization").startsWith("Bearer "))
            filterChain.doFilter(request, response);
        else {
            byte[] authorizations = Base64.getDecoder().decode(request.getHeader("Authorization"));
            Object user = redisTemplate.opsForHash().get("mugit:sessions:" + new String(authorizations), LOGIN_USER_KEY.getSessionKey());
            UserSessionDto userDto = objectMapper.convertValue(user, UserSessionDto.class);
            log.info("request user session info : {}", userDto);

            if (isNull(userDto)) filterChain.doFilter(request, response);
            else {
                Long id = userDto.getId();
                String email = userDto.getEmail();
                RoleType role = userDto.getRole();

                getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, id, role.getAuthorities()));
                filterChain.doFilter(request, response);
            }
        }
    }
}
