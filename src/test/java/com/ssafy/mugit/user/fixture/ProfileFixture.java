package com.ssafy.mugit.user.fixture;

import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;

public enum ProfileFixture {
    PROFILE(1L, "leaf", "프로필", "http://localhost:8080/profile/1"),
    PROFILE_2(2L, "leaf2", "프로필2", "http://localhost:8080/profile/2"),
    PROFILE_3(3L, "jindol", "프로필3", "http://localhost:8080/profile/3"),
    NO_INPUT_PROFILE(3L, "leaf", "", "");

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

    public Profile getFixture(){
        return new Profile(id, nickName, profileText, profileImage, null);
    }

    public Profile getFixture(User user){
        return new Profile(id, nickName, profileText, profileImage, user);
    }
}
