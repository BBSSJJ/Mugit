package com.ssafy.mugit.user.service;

import com.ssafy.mugit.user.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLogoutService {
    private final CookieUtil cookieUtil;

    public HttpHeaders logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return cookieUtil.deleteLoginCookie();
    }
}
