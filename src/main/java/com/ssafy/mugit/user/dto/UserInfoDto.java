package com.ssafy.mugit.user.dto;

import com.ssafy.mugit.global.web.dto.GoogleUserInfoDto;
import com.ssafy.mugit.user.entity.type.SnsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private String snsId;
    private SnsType snsType;
    private String email;

    public UserInfoDto(GoogleUserInfoDto googleUserInfo) {
        this.snsId = googleUserInfo.getId();
        this.snsType = SnsType.GOOGLE;
        this.email = googleUserInfo.getEmail();
    }
}
