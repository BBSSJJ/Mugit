package com.ssafy.mugit.user.service;

import com.ssafy.mugit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDeleteService {

    private final UserRepository userRepository;

    public void deleteUserEntity(Long userId) {
        userRepository.deleteById(userId);
    }
}
