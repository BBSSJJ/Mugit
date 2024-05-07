package com.ssafy.mugit.user.fixture;

import com.ssafy.mugit.global.dto.GoogleUserInfoDto;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;

import static com.ssafy.mugit.user.fixture.ProfileFixture.*;
import static com.ssafy.mugit.user.fixture.UserFixture.*;

public enum GoogleUserInfoFixture {
    GOOGLE_USER_INFO(USER.getFixture(), PROFILE.getFixture()),
    GOOGLE_USER_INFO_2(USER_2.getFixture(), PROFILE_2.getFixture()),
    NOT_REGISTERED_USER_INFO(USER_3.getFixture(), PROFILE_3.getFixture());

    private final String id;
    private final String email;
    private final String verifiedEmail;
    private final String name;
    private final String givenName;
    private final String picture;

    GoogleUserInfoFixture(User user, Profile profile) {
        this.id = user.getSnsId();
        this.email = user.getEmail();
        this.verifiedEmail = "true";
        this.name = "gildong";
        this.givenName = "hong";
        this.picture = "https://example.com/test/1234";
    }

    public GoogleUserInfoDto getFixture() {
        return new GoogleUserInfoDto(id, email, verifiedEmail, name, givenName, picture);
    }

}
