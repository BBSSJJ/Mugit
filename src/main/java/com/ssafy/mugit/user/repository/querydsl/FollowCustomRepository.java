package com.ssafy.mugit.user.repository.querydsl;

import com.ssafy.mugit.user.dto.FollowerDto;
import com.ssafy.mugit.user.entity.Follow;

import java.util.List;

public interface FollowCustomRepository {

    Long countMyFollowers(long myId);

    Long countMyFollowings(long myId);

    List<FollowerDto> findAllFollowers(long myId);

    List<FollowerDto> findAllFollowings(long myId);

    boolean existsFollow(Long followingId, Long followeeId);

    Follow findByFollowingIdAndFolloweeId(Long followingId, Long followeeId);
}
