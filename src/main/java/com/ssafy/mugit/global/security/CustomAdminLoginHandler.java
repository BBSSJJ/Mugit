package com.ssafy.mugit.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.dto.MessageDto;
import com.ssafy.mugit.global.dto.UserSessionDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.ssafy.mugit.global.dto.DataKeys.LOGIN_USER_KEY;
import static com.ssafy.mugit.user.entity.type.RoleType.ROLE_ADMIN;

@Component
public class CustomAdminLoginHandler implements AuthenticationSuccessHandler {

    private final String userId;
    private final String userPassword;
    private final ObjectMapper objectMapper;

    public CustomAdminLoginHandler( @Value("${spring.security.user.name}") String userId,
                                    @Value("${spring.security.user.password}")String userPassword,
                                    @Autowired ObjectMapper objectMapper) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 세션 설정
        UserSessionDto userSessionDto = new UserSessionDto(-1L, "admin@mugit.site", ROLE_ADMIN);
        request.getSession().setAttribute(LOGIN_USER_KEY.getKey(), userSessionDto);

        // 200 응답
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), new MessageDto("관리자 로그인 완료"));
    }
}
