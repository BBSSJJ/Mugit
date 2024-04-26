package com.ssafy.mugit.user.fixture;

import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.SnsType;

public enum UserFixture {
    DEFAULT_LOGIN_USER(1L, "asdf1234", SnsType.GOOGLE, "test@test.com"),
    DEFAULT_LOGIN_USER_2(2L, "qwer1234", SnsType.GOOGLE, "test2@test.com");

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
        return new User(id, snsId, snsType, email, null);
    }

    public User getFixture(Profile profile){
        return new User(id, snsId, snsType, email, profile);
    }

}
