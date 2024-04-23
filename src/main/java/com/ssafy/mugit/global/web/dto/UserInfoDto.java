package com.ssafy.mugit.global.web.dto;

import com.ssafy.mugit.user.entity.type.SnsType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoDto {
    private String snsId;
    private SnsType snsType;
    private String email;
}
