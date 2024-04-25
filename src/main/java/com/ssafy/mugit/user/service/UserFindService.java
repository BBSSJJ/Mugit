package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFindService {

    private final UserRepository userRepository;

    public ResponseUserProfileDto findUserByNickName(String nickName) {
        ResponseUserProfileDto userDto = userRepository.findUserProfileDtoByNickName(nickName);
        if (userDto == null) throw new UserApiException(UserApiError.NOT_FOUND);
        return userDto;
    }
}
