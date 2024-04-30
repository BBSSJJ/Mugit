package com.ssafy.mugit.global.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.user.entity.type.RoleType;
import com.ssafy.mugit.user.dto.UserSessionDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.ssafy.mugit.auth.SessionKeys.LOGIN_USER_KEY;
import static java.util.Objects.isNull;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOncePerRequestFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Object userInSession = request.getSession().getAttribute(LOGIN_USER_KEY.getKey());

        // 로그인 하지 않은 사용자 권한설정하지 않음
        if (isNull(userInSession)) filterChain.doFilter(request, response);

        // 로그인한 사용자 권한 설정
        else {
            UserSessionDto user = (UserSessionDto) userInSession;
            log.info("request user session info : {}", user);

            Long id = user.getId();
            String email = user.getEmail();
            RoleType role = user.getRole();

            getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, id, role.getAuthorities()));
            filterChain.doFilter(request, response);
        }

    }
}
