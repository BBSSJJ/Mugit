package com.ssafy.mugit.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.global.api.OAuthApi;
import com.ssafy.mugit.user.dto.UserInfoDto;
import com.ssafy.mugit.global.dto.UserSessionDto;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.repository.FollowRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.util.CookieUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssafy.mugit.global.dto.DataKeys.LOGIN_USER_KEY;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    @Qualifier("OAuthRestTemplateApi")
    private final OAuthApi oAuthApi;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final CookieUtil cookieUtil;

    @Transactional
    public HttpHeaders login(String token, SnsType snsType, HttpSession session) throws JsonProcessingException {

        // 토큰 없을 때 오류처리
        if (token == null || token.isEmpty()) throw new UserApiException(UserApiError.NO_OAUTH_TOKEN);

        // SNS 인증정보로 DB 조회
        UserInfoDto userInfo = oAuthApi.getUserInfo(getBearerToken(token), snsType);
        User userInDB = userRepository.findBySnsIdAndSnsType(userInfo.getSnsId(), userInfo.getSnsType());

        // 회원가입 필요시 regist cookie 등록
        if (userInDB == null) return cookieUtil.getRegistCookieHeader(userInfo);

        // 로그인 시 세션 설정
        UserSessionDto userSessionDto = new UserSessionDto(userInDB);
        session.setAttribute(LOGIN_USER_KEY.getKey(), userSessionDto);

        return cookieUtil.getLoginCookieHeader(userInDB,
                followRepository.countMyFollowers(userInDB.getId()),
                followRepository.countMyFollowings(userInDB.getId()));
    }

    private String getBearerToken(String token) {
        return token.substring(7);
    }
}
