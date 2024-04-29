package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDeleteService {
    private final UserRepository userRepository;

    public void deleteUserEntity(Long userId) {

        // pk 검증
        if (userId == null) throw new UserApiException(UserApiError.NOT_AUTHORIZED_USER);

        userRepository.deleteById(userId);
    }
}
