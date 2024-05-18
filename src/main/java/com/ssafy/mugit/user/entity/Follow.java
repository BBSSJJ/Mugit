package com.ssafy.mugit.user.entity;

import com.ssafy.mugit.global.entity.BaseTimeEntity;
import com.ssafy.mugit.global.exception.UserApiException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.ssafy.mugit.global.exception.error.UserApiError.SELF_FOLLOW;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    private User followee;

    public Follow(User follower, User followee) {

        // 본인 팔로우 방지
        if (follower.getId().equals(followee.getId())) throw new UserApiException(SELF_FOLLOW);

        this.follower = follower;
        this.followee = followee;
    }
}
