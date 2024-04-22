package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.global.web.api.OAuthApi;
import com.ssafy.mugit.global.web.dto.UserInfoDto;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.util.CookieUtil;
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
    public HttpHeaders loginAndGetCookieHeader(String token, SnsType snsType) {
        UserInfoDto userInfo = getUserInfo(getBearerToken(token), snsType);
        User userInDB = getUser(userInfo);
        return getLoginCookieHeader(userInDB);
    }

    public UserInfoDto getUserInfo(String token, SnsType snsType) {
        return oAuthApi.getUserInfo(token, snsType);
    }

    public User getUser(UserInfoDto userInfo) {
        User userInDB = userRepository.findBySnsIdAndSnsType(userInfo.getSnsId(), userInfo.getSnsType());
        if (userInDB == null) throw new UserApiException(UserApiError.NOT_REGISTERED_USER);
        return userInDB;
    }

    public HttpHeaders getLoginCookieHeader(User user) {
        return cookieUtil.getLoginCookieHeader(user);
    }

    public String getBearerToken(String token) {
        return token.substring(7);
    }
}
