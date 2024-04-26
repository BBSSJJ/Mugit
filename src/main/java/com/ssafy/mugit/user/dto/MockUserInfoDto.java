package com.ssafy.mugit.user.dto;

import com.ssafy.mugit.user.entity.type.SnsType;
import lombok.Data;

@Data
public class MockUserInfoDto {

    private String snsId;
    private SnsType snsType;
    private String email;
    private String nickName;
    private String profileText;
    private String profileImagePath;

    public MockUserInfoDto(SnsType snsType, String email, String nickName, String profileText, String profileImagePath) {
        this.snsType = snsType;
        this.email = email;
        this.nickName = nickName;
        this.profileText = profileText;
        this.profileImagePath = profileImagePath;
    }
}