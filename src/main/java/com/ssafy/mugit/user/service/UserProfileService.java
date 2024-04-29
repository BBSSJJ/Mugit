package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.user.dto.request.RequestModifyUserInfoDto;
import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.repository.ProfileRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public ResponseUserProfileDto getProfileById(Long userId) {

        // 해당 프로필 조회
        ResponseUserProfileDto userDto = userRepository.findUserProfileDtoByUserId(userId);
        if (userDto == null) throw new UserApiException(UserApiError.NOT_FOUND);
        return userDto;
    }

    @Transactional
    public void updateProfile(Long userId, RequestModifyUserInfoDto dto) {

        // pk 검사
        System.out.println("userId = " + userId);
        if (userId == null) throw new UserApiException(UserApiError.NOT_AUTHORIZED_USER);

        // 중복 검사
        if (profileRepository.existsByNickName(dto.getNickName()))
            throw new UserApiException(UserApiError.DUPLICATE_NICK_NAME);

        // 업데이트
        Profile profileInDB = profileRepository.findByUserId(userId);
        profileInDB.update(dto.getNickName(), dto.getProfileText(), dto.getProfileImagePath());
    }
}
