package com.ssafy.mugit.user.service;

import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;

    public ResponseUserProfileDto getProfileById(Long userId) {
        return userRepository.findUserProfileDtoByUserId(userId);
    }
}
