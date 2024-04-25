package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.auth.SessionKeys;
import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.service.UserProfileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/profiles/detail")
    public ResponseEntity<ResponseUserProfileDto> getMyProfile(HttpSession session) {
        Long myId = (Long) session.getAttribute(SessionKeys.LOGIN_USER_SESSION_ID.getKey());
        return ResponseEntity.ok()
                .body(userProfileService.getProfileById(myId));
    }

    @GetMapping("/{userId}/profiles/detail")
    public ResponseEntity<ResponseUserProfileDto> getUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok()
                .body(userProfileService.getProfileById(userId));
    }
}
