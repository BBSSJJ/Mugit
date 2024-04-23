package com.ssafy.mugit.user.fixture;

import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;

public enum ProfileFixture {
    DEFAULT_PROFILE(1L, "leaf", "프로필", "http://localhost:8080/profile/1"),
    NO_INPUT_PROFILE(1L, "leaf", "", "");

    private final Long id;
    private final String nickName;
    private final String profileText;
    private final String profileImage;

    ProfileFixture(Long id, String nickName, String profileText, String profileImage) {
        this.id = id;
        this.nickName = nickName;
        this.profileText = profileText;
        this.profileImage = profileImage;
    }

    public Profile getProfile(){
        return new Profile(id, nickName, profileText, profileImage, null);
    }

    public Profile getProfile(User user){
        return new Profile(id, nickName, profileText, profileImage, user);
    }
}
