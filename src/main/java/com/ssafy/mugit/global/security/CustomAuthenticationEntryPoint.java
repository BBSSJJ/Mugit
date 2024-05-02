package com.ssafy.mugit.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.dto.SecurityExceptionDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    private static final SecurityExceptionDto exceptionDto =
            new SecurityExceptionDto(HttpStatus.UNAUTHORIZED.value(), "Security Filter 통과 실패 : 로그인 정보 없음");

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authenticationException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        try (OutputStream os = response.getOutputStream()) {
            objectMapper.writeValue(os, exceptionDto);
            os.flush();
        }
    }
}