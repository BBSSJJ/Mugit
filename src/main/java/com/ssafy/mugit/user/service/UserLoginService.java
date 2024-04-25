package com.ssafy.mugit.user.service;

import com.ssafy.mugit.auth.SessionKeys;
import com.ssafy.mugit.global.web.api.OAuthApi;
import com.ssafy.mugit.user.dto.UserInfoDto;
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
    public HttpHeaders login(String token, SnsType snsType, HttpSession session) {
        UserInfoDto userInfo = oAuthApi.getUserInfo(getBearerToken(token), snsType);
        User userInDB = userRepository.findBySnsIdAndSnsType(userInfo.getSnsId(), userInfo.getSnsType());

        // 회원가입 필요시
        if (userInDB == null) return cookieUtil.getRegistCookieHeader(userInfo);

        session.setAttribute(SessionKeys.LOGIN_USER_SESSION_ID.getKey(), userInDB.getId());
        return cookieUtil.getLoginCookieHeader(userInDB);
    }

    public String getBearerToken(String token) {
        return token.substring(7);
    }
}
