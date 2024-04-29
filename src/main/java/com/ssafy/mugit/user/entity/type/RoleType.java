package com.ssafy.mugit.user.entity.type;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static com.ssafy.mugit.user.entity.type.RoleAuthority.*;
import static java.util.Arrays.asList;

public enum RoleType {
    ROLE_USER(asList(DEFAULT_USER_AUTHORITY)),
    ROLE_ADMIN(asList(DEFAULT_ADMIN_AUTHORITY));

    private final Collection<GrantedAuthority> authorities;

    RoleType(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
