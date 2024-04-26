package com.ssafy.mugit.user.service;

import com.ssafy.mugit.auth.SessionKeys;
import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
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

        // 토큰 없을 때 오류처리
        if (token == null || token.isEmpty()) throw new UserApiException(UserApiError.NO_OAUTH_TOKEN);

        // SNS 인증정보로 DB 조회
        UserInfoDto userInfo = oAuthApi.getUserInfo(getBearerToken(token), snsType);
        User userInDB = userRepository.findBySnsIdAndSnsType(userInfo.getSnsId(), userInfo.getSnsType());

        // 회원가입 필요시 regist cookie 등록
        if (userInDB == null) return cookieUtil.getRegistCookieHeader(userInfo);

        // 로그인 시 세션 설정
        session.setAttribute(SessionKeys.LOGIN_USER_SESSION_ID.getKey(), userInDB.getId());
        return cookieUtil.getLoginCookieHeader(userInDB);
    }

    private String getBearerToken(String token) {
        return token.substring(7);
    }
}
