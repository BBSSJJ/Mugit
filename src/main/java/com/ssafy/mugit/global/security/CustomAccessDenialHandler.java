package com.ssafy.mugit.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.global.dto.SecurityExceptionDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
@RequiredArgsConstructor
public class CustomAccessDenialHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    private static final SecurityExceptionDto exceptionDto =
            new SecurityExceptionDto(HttpStatus.FORBIDDEN.value(), "Security Filter 통과 실패 : 권한 부족");

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        try (OutputStream os = response.getOutputStream()) {
            objectMapper.writeValue(os, exceptionDto);
            os.flush();
        }
    }
}