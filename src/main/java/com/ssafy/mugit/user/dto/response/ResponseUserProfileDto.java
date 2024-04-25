package com.ssafy.mugit.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import lombok.Data;

@Data
public class ResponseUserProfileDto {
    private Long id;
    private String snsId;
    private String snsType;
    private String email;
    private String nickName;
    private String profileText;
    private String profileImagePath;

    @QueryProjection
    public ResponseUserProfileDto(User user, Profile profile) {
        this.id = user.getId();
        this.snsId = user.getSnsId();
        this.snsType = user.getSnsType().name();
        this.email = user.getEmail();
        this.nickName = profile.getNickName();
        this.profileText = profile.getProfileText();
        this.profileImagePath = profile.getProfileImagePath();
    }
}
