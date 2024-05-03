package com.ssafy.mugit.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.user.dto.MockUserInfoDto;
import com.ssafy.mugit.global.dto.UserSessionDto;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.FollowRepository;
import com.ssafy.mugit.user.repository.ProfileRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.util.CookieUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssafy.mugit.global.dto.DataKeys.LOGIN_USER_KEY;

@Service
@RequiredArgsConstructor
public class MockUserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final FollowRepository followRepository;
    private final CookieUtil cookieUtil;

    @Transactional
    public User createUserByUserInfo(MockUserInfoDto userInfo) {
        // 중복검사
        validateDuplicate(userInfo);

        // 엔티티 생성
        User user = new User(null, userInfo.getEmail(), userInfo.getSnsType());
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

    public HttpHeaders login(Long userPk, HttpSession session) throws JsonProcessingException {

        // DB에 해당 사용자 없을 때
        User userInDB = userRepository.findById(userPk)
                .orElseThrow(() -> new UserApiException(UserApiError.NOT_FOUND));

        // 세션에 해당 사용자 기록
        UserSessionDto userSessionDto = new UserSessionDto(userInDB);
        session.setAttribute(LOGIN_USER_KEY.getKey(), userSessionDto);
        return cookieUtil.getLoginCookieHeader(userInDB,
                followRepository.countMyFollowers(userInDB.getId()),
                followRepository.countMyFollowings(userInDB.getId()));
    }
}
