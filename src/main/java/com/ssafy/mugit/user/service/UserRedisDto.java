package com.ssafy.mugit.user.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRedisDto {
    private Long id;
    private String email;
    private RoleType role;

    public UserRedisDto(User userInDB) {
        this.id = userInDB.getId();
        this.email = userInDB.getEmail();
        this.role = userInDB.getRole();
    }

    public UserRedisDto(Long id, String email, RoleType role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }
}
