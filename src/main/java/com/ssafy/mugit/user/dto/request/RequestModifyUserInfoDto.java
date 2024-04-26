package com.ssafy.mugit.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestModifyUserInfoDto {
    private String nickName;
    private String profileText;
    private String profileImagePath;
}
