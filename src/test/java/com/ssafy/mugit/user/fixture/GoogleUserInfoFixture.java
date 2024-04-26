package com.ssafy.mugit.user.fixture;

import com.ssafy.mugit.global.web.dto.GoogleUserInfoDto;

public enum GoogleUserInfoFixture {
    DEFAULT_GOOGLE_USER_INFO("asdf1234", "test@test.com", "true", "sangyeop", "nam", "test/1234"),
    NOT_REGISTERED_USER_INFO("not registered sns id", "test@test.com", "true", "sangyeop", "nam", "test/1234");

    private final String id;
    private final String email;
    private final String verifiedEmail;
    private final String name;
    private final String givenName;
    private final String picture;

    GoogleUserInfoFixture(String id, String email, String verifiedEmail, String name, String givenName, String picture) {
        this.id = id;
        this.email = email;
        this.verifiedEmail = verifiedEmail;
        this.name = name;
        this.givenName = givenName;
        this.picture = picture;
    }

    public GoogleUserInfoDto getFixture() {
        return new GoogleUserInfoDto(id, email, verifiedEmail, name, givenName, picture);
    }

}
