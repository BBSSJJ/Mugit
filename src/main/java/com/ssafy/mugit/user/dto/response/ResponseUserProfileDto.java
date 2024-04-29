package com.ssafy.mugit.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
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

    // 관리자 DTO 생성용
    public ResponseUserProfileDto(boolean isAdmin) {
        if (!isAdmin) throw new UserApiException(UserApiError.NOT_REGISTERED_USER);
        this.id = -1L;
        this.snsId = null;
        this.snsType = null;
        this.email = "admin@mugit.site";
        this.nickName = "admin";
        this.profileText = "";
        this.profileImagePath = "";
    }
}
