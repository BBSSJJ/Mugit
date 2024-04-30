package com.ssafy.mugit.user.fixture;

import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.RoleType;
import com.ssafy.mugit.user.entity.type.SnsType;

public enum UserFixture {
    USER(1L, "asdf1234", SnsType.GOOGLE, "test@test.com"),
    USER_2(2L, "qwer1234", SnsType.GOOGLE, "test2@test.com"),
    USER_3(3L, "asdfzxcv1234", SnsType.GOOGLE, "test3@test.com");

    private final Long id;
    private final String snsId;
    private final SnsType snsType;
    private final String email;

    UserFixture(Long id, String snsId, SnsType snsType, String email) {
        this.id = id;
        this.snsId = snsId;
        this.snsType = snsType;
        this.email = email;
    }

    public User getFixture(){
        return new User(id, snsId, email, snsType, RoleType.ROLE_USER, null);
    }

    public User getFixture(Profile profile){
        User user = new User(id, snsId, email, snsType, RoleType.ROLE_USER, null);
        user.regist(profile);
        return user;
    }

}
