package com.ssafy.mugit.user.service;

import com.ssafy.mugit.auth.SessionKeys;
import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.user.dto.request.RegistProfileDto;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.repository.ProfileRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final CookieUtil cookieUtil;

    public HttpHeaders registAndSetLogin(String snsId, SnsType snsType, String email, RegistProfileDto registProfileDto, HttpServletRequest request) {

        // 중복검사
        validateDuplicate(registProfileDto);

        // 회원가입
        User registeredUser = new User(snsId, snsType, email);
        registeredUser.regist(getProfile(registProfileDto));
        userRepository.save(registeredUser);

        // 로그인
        request.getSession().setAttribute(SessionKeys.LOGIN_USER_SESSION_ID.getKey(), registeredUser.getId());

        // 로그인 쿠키 + 회원가입 쿠키 초기화
        return cookieUtil.getLoginCookieAndRemoveRegistCookieHeader(registeredUser);
    }

    public void validateDuplicate(RegistProfileDto registProfileDto) {
        if (profileRepository.existsByNickName(registProfileDto.getNickName()))
            throw new UserApiException(UserApiError.DUPLICATE_NICK_NAME);
    }

    private Profile getProfile(RegistProfileDto registProfileDto) {
        return new Profile(registProfileDto.getNickName(), registProfileDto.getProfileText(), registProfileDto.getProfileImage());
    }

}
