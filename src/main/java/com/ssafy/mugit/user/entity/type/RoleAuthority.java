package com.ssafy.mugit.user.entity.type;

import org.springframework.security.core.GrantedAuthority;

public enum RoleAuthority implements GrantedAuthority {
    DEFAULT_USER_AUTHORITY("사용자 기본 권한"),
    DEFAULT_ADMIN_AUTHORITY("관리자 기본 권한");

    private final String authority;
    
    RoleAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
