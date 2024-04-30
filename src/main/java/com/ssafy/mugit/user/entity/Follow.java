package com.ssafy.mugit.user.entity;

import com.ssafy.mugit.global.exception.UserApiException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.ssafy.mugit.global.exception.error.UserApiError.SELF_FOLLOW;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    private User following;

    public Follow(User follower, User following) {

        // 본인 팔로우 방지
        if (follower.getId().equals(following.getId())) throw new UserApiException(SELF_FOLLOW);

        this.follower = follower;
        this.following = following;
    }
}
