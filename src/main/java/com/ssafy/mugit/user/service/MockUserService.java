package com.ssafy.mugit.user.service;

import com.ssafy.mugit.auth.SessionKeys;
import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.user.dto.MockUserInfoDto;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.ProfileRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.util.CookieUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MockUserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final CookieUtil cookieUtil;

    @Transactional
    public User createUserByUserInfo(MockUserInfoDto userInfo) {
        // 중복검사
        validateDuplicate(userInfo);

        // 엔티티 생성
        User user = new User(null, userInfo.getSnsType(), userInfo.getEmail());
        Profile profile = new Profile(userInfo.getNickName(), userInfo.getProfileText(), userInfo.getProfileImagePath());
        user.regist(profile);

        // DB 저장
        userRepository.save(user);

        // 임시 Id 세팅
        user.makeMockSnsId();

        return user;
    }

    private void validateDuplicate(MockUserInfoDto userInfo) {
        if (profileRepository.existsByNickName(userInfo.getNickName()))
            throw new UserApiException(UserApiError.DUPLICATE_NICK_NAME);
    }

    public HttpHeaders login(Long userPk, HttpSession session) {
        User userInDB = userRepository.findById(userPk)
                .orElseThrow(() -> new UserApiException(UserApiError.NOT_FOUND));
        session.setAttribute(SessionKeys.LOGIN_USER_SESSION_ID.getKey(), userInDB.getId());
        return cookieUtil.getLoginCookieHeader(userInDB);
    }
}
