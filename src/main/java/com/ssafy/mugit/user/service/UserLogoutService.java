package com.ssafy.mugit.user.service;

import com.ssafy.mugit.user.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLogoutService {
    private final CookieUtil cookieUtil;

    public HttpHeaders logout(HttpSession session) {
        
        // 로그아웃 시 세션 제거
        session.invalidate();
        
        // 로그인 쿠키 삭제
        return cookieUtil.deleteLoginCookie();
    }
}
