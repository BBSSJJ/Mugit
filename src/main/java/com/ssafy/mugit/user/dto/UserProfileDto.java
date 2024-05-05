package com.ssafy.mugit.user.dto;

import com.ssafy.mugit.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    Long id;
    String nickName;
    String profileImagePath;

    public UserProfileDto(User user) {
        this.id = user.getId();
        this.nickName = user.getProfile().getNickName();
        this.profileImagePath = user.getProfile().getProfileImagePath();
    }
}
