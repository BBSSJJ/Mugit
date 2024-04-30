package com.ssafy.mugit.user.fixture;

import com.ssafy.mugit.user.dto.FollowerDto;
import com.ssafy.mugit.user.entity.User;

import static com.ssafy.mugit.user.fixture.ProfileFixture.PROFILE_2;
import static com.ssafy.mugit.user.fixture.ProfileFixture.PROFILE_3;
import static com.ssafy.mugit.user.fixture.UserFixture.USER_2;
import static com.ssafy.mugit.user.fixture.UserFixture.USER_3;

public enum FollowerFixture {
    FOLLOWER_USER_2(USER_2.getFixture(PROFILE_2.getFixture())),
    FOLLOWER_USER_3(USER_3.getFixture(PROFILE_3.getFixture()));

    private final Long followerId;
    private final String followerNickName;
    private final String followerProfileText;
    private final String followerProfileImagePath;

    FollowerFixture(User user) {
        this.followerId = user.getId();
        this.followerNickName = user.getProfile().getNickName();
        this.followerProfileText = user.getProfile().getProfileText();
        this.followerProfileImagePath = user.getProfile().getProfileImagePath();
    }

    public FollowerDto getFixture() {
        return new FollowerDto(followerId, followerNickName, followerProfileText, followerProfileImagePath);
    }
}
