package com.ssafy.mugit.fixure;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlowFixture {
    FLOW("test title", "https://mugit.site/files/default_mugic_path"),
    FLOW_2("test title 2", "https://mugit.site/files/default_mugic_path");

    private final String title;
    private final String musicPath;

    public Flow getFixture(User user) {
        return new Flow(user, title, musicPath);
    }
}
