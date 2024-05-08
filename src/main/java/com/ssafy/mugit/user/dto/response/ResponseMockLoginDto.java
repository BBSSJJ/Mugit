package com.ssafy.mugit.user.dto.response;

import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.net.HttpCookie;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class ResponseMockLoginDto {
    private String sessionId;
    private Long userId;
    private String nickName;
    private String profileText;
    private String profileImagePath;
    private Long followers;
    private Long followings;

    public ResponseMockLoginDto(User user, String sessionId, Long followers, Long followings) {
        this.sessionId = Base64.getEncoder().encodeToString(sessionId.getBytes());
        this.userId = user.getId();
        this.nickName = user.getProfile().getNickName();
        this.profileText = user.getProfile().getProfileText();
        this.profileImagePath = user.getProfile().getProfileImagePath();
        this.followers = followers;
        this.followings = followings;
    }
}
