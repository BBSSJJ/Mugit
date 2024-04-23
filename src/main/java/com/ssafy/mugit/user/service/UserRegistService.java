package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.user.dto.request.RegistProfileDto;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.repository.ProfileRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final CookieUtil cookieUtil;

    public HttpHeaders registAndGetCookieHeader(String snsId, SnsType snsType, String email, RegistProfileDto registProfileDto) {

        // 중복검사
        validateDuplicate(registProfileDto);

        User userInDB = userRepository.findBySnsIdAndSnsType(snsId, snsType);
        getProfile(registProfileDto);
        regist(userInDB, getProfile(registProfileDto));
        return cookieUtil.getLoginCookieHeader(userInDB);
    }

    public void validateDuplicate(RegistProfileDto registProfileDto) {
        if (profileRepository.existsByNickName(registProfileDto.getNickName()))
            throw new UserApiException(UserApiError.DUPLICATE_NICK_NAME);
    }

    public void regist(User user, Profile profile) {
        user.regist(profile);
        userRepository.save(user);
    }

    private Profile getProfile(RegistProfileDto registProfileDto) {
        return new Profile(registProfileDto.getNickName(), registProfileDto.getProfileText(), registProfileDto.getProfileImage());
    }

    public HttpHeaders getLoginCookieHeader(User user) {
        return cookieUtil.getLoginCookieHeader(user);
    }
}
