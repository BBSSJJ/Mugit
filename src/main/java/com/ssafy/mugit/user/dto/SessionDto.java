package com.ssafy.mugit.user.dto;

import com.ssafy.mugit.user.entity.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionDto {
    private Long id;
    private String email;
    private RoleType role;
}
