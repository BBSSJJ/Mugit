package com.ssafy.mugit.user.util;

import com.ssafy.mugit.user.dto.UserInfoDto;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    @Value("${cookie.domain}")
    private String domainUrl;

    @Value("${server.servlet.session.timeout}")
    private long sessionTimeout;

    public HttpHeaders getLoginCookieHeader(User user, Long followers, Long followings) {

        HttpHeaders cookieHeaders = new HttpHeaders();
        Profile profile = user.getProfile();

        setTimeoutCookieHeader(cookieHeaders, "isLogined", "true");
        setTimeoutCookieHeader(cookieHeaders, "nickName", profile.getNickName());
        setTimeoutCookieHeader(cookieHeaders, "profileText", profile.getProfileText());
        setTimeoutCookieHeader(cookieHeaders, "profileImage", profile.getProfileImagePath());
        setTimeoutCookieHeader(cookieHeaders, "followers", followers.toString());
        setTimeoutCookieHeader(cookieHeaders, "followings", followings.toString());

        return cookieHeaders;
    }

    public HttpHeaders getRegistCookieHeader(UserInfoDto userInfo) {

        HttpHeaders cookieHeaders = new HttpHeaders();

        setSessionCookieHeader(cookieHeaders, "needRegist", "true");
        setSessionCookieHeader(cookieHeaders, "snsId", userInfo.getSnsId());
        setSessionCookieHeader(cookieHeaders, "snsType", userInfo.getSnsType().toString());
        setSessionCookieHeader(cookieHeaders, "email", userInfo.getEmail());

        return cookieHeaders;
    }

    public HttpHeaders getLoginCookieAndDeleteRegistCookieHeader(User user, Long followers, Long followings) {

        HttpHeaders cookieHeaders = getLoginCookieHeader(user, followers, followings);

        // 회원가입 쿠키 삭제
        setDeleteCookieHeader(cookieHeaders, "needRegist");
        setDeleteCookieHeader(cookieHeaders, "snsId");
        setDeleteCookieHeader(cookieHeaders, "snsType");
        setDeleteCookieHeader(cookieHeaders, "email");

        return cookieHeaders;
    }

    public HttpHeaders deleteLoginCookie() {

        HttpHeaders cookieHeaders = new HttpHeaders();

        setDeleteCookieHeader(cookieHeaders, "isLogined");
        setDeleteCookieHeader(cookieHeaders, "nickName");
        setDeleteCookieHeader(cookieHeaders, "profileText");
        setDeleteCookieHeader(cookieHeaders, "profileImage");

        return cookieHeaders;
    }

    public ResponseCookie getTimeoutCookie(String key, String value) {
        return ResponseCookie.from(key, URLEncoder.encode(value, StandardCharsets.UTF_8))
                .path("/")
                .domain(domainUrl)
                .sameSite("None")
                .secure(true)
                .maxAge(sessionTimeout)
                .build();
    }

    public ResponseCookie getSessionCookie(String key, String value) {
        return ResponseCookie.from(key, URLEncoder.encode(value, StandardCharsets.UTF_8))
                .path("/")
                .domain(domainUrl)
                .sameSite("None")
                .secure(true)
                .build();
    }

    private void setTimeoutCookieHeader(HttpHeaders cookieHeaders, String key, String value) {
        cookieHeaders.add(HttpHeaders.SET_COOKIE, getTimeoutCookie(key, value).toString());
    }

    private void setSessionCookieHeader(HttpHeaders cookieHeaders, String key, String value) {
        cookieHeaders.add(HttpHeaders.SET_COOKIE, getSessionCookie(key, value).toString());
    }

    private void setDeleteCookieHeader(HttpHeaders cookieHeaders, String key) {
        cookieHeaders.add(HttpHeaders.SET_COOKIE, ResponseCookie.from(key, "")
                .path("/")
                .maxAge(0)
                .domain(domainUrl)
                .build().toString());
    }
}
