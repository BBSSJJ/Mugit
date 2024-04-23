package com.ssafy.mugit.user.service;

import com.ssafy.mugit.user.util.CookieUtil;
import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.user.dto.request.RegistProfileDto;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.repository.ProfileRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final CookieUtil cookieUtil;

    public HttpHeaders registAndGetLoginCookieHeader(String snsId, SnsType snsType, RegistProfileDto registProfileDto) {
        if (profileRepository.existsByNickName(registProfileDto.getNickName()))
            throw new UserApiException(UserApiError.DUPLICATE_NICK_NAME);

        User user = userRepository.findBySnsIdAndSnsType(snsId, snsType);
        user.regist(new Profile(registProfileDto.getNickName(), registProfileDto.getProfileText(), registProfileDto.getProfileImage()));
        return cookieUtil.getLoginCookieHeader(user);
    }
}
