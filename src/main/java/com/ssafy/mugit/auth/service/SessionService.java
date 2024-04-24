package com.ssafy.mugit.auth.service;

import com.ssafy.mugit.auth.SessionKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SessionService {

    public HttpSession createSession(HttpServletRequest request, Long id) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionKeys.LOGIN_USER_ID.getKey(), id);
        return session;
    }

    public Long getSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (Long) session.getAttribute(SessionKeys.LOGIN_USER_ID.getKey());
    }

}
