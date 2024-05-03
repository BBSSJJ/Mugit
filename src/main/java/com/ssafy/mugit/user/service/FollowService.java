package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.user.dto.FollowerDto;
import com.ssafy.mugit.user.entity.Follow;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.FollowRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssafy.mugit.global.exception.error.UserApiError.ALREADY_FOLLOW;
import static com.ssafy.mugit.global.exception.error.UserApiError.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final NotificationService notificationService;

    @Transactional
    public void follow(Long followingId, Long followerId) {

        // 팔로우 여부 확인
        if (followRepository.existsFollow(followingId, followerId)) throw new UserApiException(ALREADY_FOLLOW);

        // 팔로워, 팔로우 찾아오기
        User follower = userRepository.findById(followingId).orElseThrow(() -> new UserApiException(NOT_FOUND));
        User following = userRepository.findById(followerId).orElseThrow(() -> new UserApiException(NOT_FOUND));

        Follow follow = new Follow(follower, following);
        followRepository.save(follow);

        // 팔로우 알림 발송
        notificationService.sendFollow(follower, following);
    }

    public void unfollow(Long followingId, Long followerId) {
        Follow followInDB = followRepository.findByFollowerIdAndFollowingId(followingId, followerId);
        if (followInDB == null) throw new UserApiException(UserApiError.NOT_EXIST_FOLLOW);

        followRepository.delete(followInDB);
    }

    public Long getAllFollowerCount(long myId) {
        return followRepository.countMyFollowers(myId);
    }

    public Long getAllFollowingCount(long myId) {
        return followRepository.countMyFollowings(myId);
    }

    public List<FollowerDto> getAllFollower(long myId) {
        return followRepository.findAllFollowers(myId);
    }

    public List<FollowerDto> getAllFollowings(long myId) {
        return followRepository.findAllFollowings(myId);
    }

    public Boolean checkIsFollower(Long followingId, Long followerId) {
        return followRepository.existsFollow(followingId, followerId);
    }
}
