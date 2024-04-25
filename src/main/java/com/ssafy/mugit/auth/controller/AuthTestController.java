package com.ssafy.mugit.auth.controller;

import com.ssafy.mugit.auth.SessionKeys;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth/test")
public class AuthTestController {

    @GetMapping("/jsessionid")
    public String login(HttpSession session) {
        session.setAttribute(SessionKeys.LOGIN_USER_SESSION_ID.getKey(), 1L);
        return session.getId();
    }

    @GetMapping("/no_jsessionid")
    public String loginWithoutSessionId() {
        return "JSESSIONID 미생성";
    }

    @GetMapping("/jsessionid/session")
    public String getSessionById(HttpSession session) {
        log.info("getSessionById : {}", session.getAttribute(SessionKeys.LOGIN_USER_SESSION_ID.getKey()));
        return "session 조회";
    }
}
