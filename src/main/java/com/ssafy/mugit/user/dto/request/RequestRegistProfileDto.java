package com.ssafy.mugit.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestRegistProfileDto {
    private String nickName;
    private String profileText;
    private String profileImage;
}
