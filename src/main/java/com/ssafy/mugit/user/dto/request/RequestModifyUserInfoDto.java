package com.ssafy.mugit.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestModifyUserInfoDto {

    private String nickName;
    private String profileText;
    private String profileImagePath;
}
