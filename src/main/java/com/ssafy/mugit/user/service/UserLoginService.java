package com.ssafy.mugit.user.service;

import com.ssafy.mugit.auth.SessionKeys;
import com.ssafy.mugit.global.web.api.OAuthApi;
import com.ssafy.mugit.global.web.dto.UserInfoDto;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.util.CookieUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    @Qualifier("OAuthRestTemplateApi")
    private final OAuthApi oAuthApi;
    private final UserRepository userRepository;
    private final CookieUtil cookieUtil;

    @Transactional
    public HttpHeaders login(String token, SnsType snsType, HttpSession httpSession) {
        UserInfoDto userInfo = getUserInfo(getBearerToken(token), snsType);
        User userInDB = getUser(userInfo);

        // 회원가입 필요시
        if (userInDB == null) return getRegistCookie(userInfo);

        httpSession.setAttribute(SessionKeys.LOGIN_USER_ID.getKey(), userInDB.getId());
        return getLoginCookieHeader(userInDB);
    }

    public UserInfoDto getUserInfo(String token, SnsType snsType) {
        return oAuthApi.getUserInfo(token, snsType);
    }

    public User getUser(UserInfoDto userInfo) {
        return userRepository.findBySnsIdAndSnsType(userInfo.getSnsId(), userInfo.getSnsType());
    }

    public HttpHeaders getLoginCookieHeader(User user) {
        return cookieUtil.getLoginCookieHeader(user);
    }

    public HttpHeaders getRegistCookie(UserInfoDto userInfo) {
        return cookieUtil.getRegistCookieHeader(userInfo);
    }

    public String getBearerToken(String token) {
        return token.substring(7);
    }
}
