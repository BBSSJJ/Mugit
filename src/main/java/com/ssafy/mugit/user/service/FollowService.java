package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.notification.service.NotificationService;
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
    public void follow(Long followingId, Long followeeId) {

        // 팔로우 여부 확인
        if (followRepository.existsFollow(followingId, followeeId)) throw new UserApiException(ALREADY_FOLLOW);

        // 팔로잉(팔로우 하는 사람), 팔로이(팔로우 당하는 사람) 찾아오기
        User following = userRepository.findById(followingId).orElseThrow(() -> new UserApiException(NOT_FOUND));
        User followee = userRepository.findById(followeeId).orElseThrow(() -> new UserApiException(NOT_FOUND));

        Follow follow = new Follow(following, followee);
        followRepository.save(follow);

        // 팔로우 알림 발송
        notificationService.sendFollow(following, followee);
    }

    public void unfollow(Long followingId, Long followeeId) {
        Follow followInDB = followRepository.findByFollowingIdAndFolloweeId(followingId, followeeId);
        if (followInDB == null) throw new UserApiException(UserApiError.NOT_EXIST_FOLLOW);

        followRepository.delete(followInDB);
    }

    public List<FollowerDto> getAllFollowers(long myId) {
        return followRepository.findAllFollowers(myId);
    }

    public List<FollowerDto> getAllFollowings(long myId) {
        return followRepository.findAllFollowings(myId);
    }

    public Boolean checkIsFollower(Long followingId, Long followeeId) {
        return followRepository.existsFollow(followingId, followeeId);
    }

    public Long countMyFollowers(Long id) {
        return followRepository.countMyFollowers(id);
    }

    public Long countMyFollowings(Long id) {
        return followRepository.countMyFollowings(id);
    }
}
