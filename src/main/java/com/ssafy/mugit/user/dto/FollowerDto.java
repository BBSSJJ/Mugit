package com.ssafy.mugit.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.ssafy.mugit.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerDto {
    private Long followerId;
    private String followerNickName;
    private String followerProfileText;
    private String followerProfileImagePath;

    @QueryProjection
    public FollowerDto(User follower) {
        this.followerId = follower.getId();
        this.followerNickName = follower.getProfile().getNickName();
        this.followerProfileText = follower.getProfile().getProfileText();
        this.followerProfileImagePath = follower.getProfile().getProfileImagePath();
    }
}
