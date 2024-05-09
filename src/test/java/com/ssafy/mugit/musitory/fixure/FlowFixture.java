package com.ssafy.mugit.musitory.fixure;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.ssafy.mugit.user.fixture.UserFixture.USER;

@Getter
@AllArgsConstructor
public enum FlowFixture {
    FLOW(USER.getFixture(), "test title", "https://mugit.site/files/default_mugic_path");

    private final User user;
    private final String title;
    private final String musicPath;

    public Flow getFixture() {
        return new Flow(user, title, musicPath);
    }
}
