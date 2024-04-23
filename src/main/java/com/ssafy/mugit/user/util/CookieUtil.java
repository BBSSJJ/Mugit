package com.ssafy.mugit.user.util;

import com.ssafy.mugit.global.web.dto.UserInfoDto;
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

    private final JwtTokenUtil tokenService;

    @Value("${cookie.domain}")
    private String domainUrl;

    @Value("${cookie.period.access-token}")
    private long periodAccessTokenCookie;

    @Value("${cookie.period.refresh-token}")
    private long periodRefreshTokenCookie;

    public HttpHeaders getLoginCookieHeader(User user) {
        HttpHeaders cookieHeaders = new HttpHeaders();
        Profile profile = user.getProfile();

        cookieHeaders.add(HttpHeaders.SET_COOKIE, getUserInfoCookie("isLogined", "true").toString());
        cookieHeaders.add(HttpHeaders.SET_COOKIE, getUserInfoCookie("nickName", profile.getNickName()).toString());
        cookieHeaders.add(HttpHeaders.SET_COOKIE, getUserInfoCookie("profileText", profile.getProfileText()).toString());
        cookieHeaders.add(HttpHeaders.SET_COOKIE, getUserInfoCookie("profileImage", profile.getProfileImagePath()).toString());

        return cookieHeaders;
    }

    public HttpHeaders getRegistCookieHeader(UserInfoDto userInfo) {
        HttpHeaders cookieHeaders = new HttpHeaders();

        cookieHeaders.add(HttpHeaders.SET_COOKIE, getRegistCookie("needRegist", "true").toString());
        cookieHeaders.add(HttpHeaders.SET_COOKIE, getRegistCookie("snsId", userInfo.getSnsId()).toString());
        cookieHeaders.add(HttpHeaders.SET_COOKIE, getRegistCookie("snsType", userInfo.getSnsType().toString()).toString());
        cookieHeaders.add(HttpHeaders.SET_COOKIE, getRegistCookie("email", userInfo.getEmail()).toString());

        return cookieHeaders;
    }

    public ResponseCookie getUserInfoCookie(String key, String value) {
        return ResponseCookie.from(key, URLEncoder.encode(value, StandardCharsets.UTF_8))
                .path("/")
                .domain(domainUrl)
                .sameSite("None")
                .secure(true)
                .maxAge(periodAccessTokenCookie)
                .build();
    }

    public ResponseCookie getRegistCookie(String key, String value) {
        return ResponseCookie.from(key, URLEncoder.encode(value, StandardCharsets.UTF_8))
                .path("/")
                .domain(domainUrl)
                .sameSite("None")
                .secure(true)
                .maxAge(600)
                .build();
    }


//    public HttpHeaders deleteRegistCookieHeader(HttpHeaders cookieHeader) {
//        HttpHeaders headers = new HttpHeaders();
//        List<String> cookies = cookieHeader.get(HttpHeaders.SET_COOKIE);
//        Objects.requireNonNull(cookies).forEach(cookie -> {
//            headers.remove(cookie);
//        });
//        return ;
//    }
//
//    public ResponseCookie removeCookie(String cookie) {
//        return ResponseCookie.from(key, "")
//                .path("/")
//                .domain(domainUrl)
//                .sameSite("None")
//                .secure(true)
//                .maxAge(0)
//                .build();
//    }

}
