package com.ssafy.mugit.flow.review.entity;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.global.entity.BaseTimeEntity;
import com.ssafy.mugit.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "review")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flow_id")
    Flow flow;

    @Column(name = "content")
    String content;

    @Column(name = "timeline")
    String timeline;

    public Review(User user, Flow flow, String content, String timeline) {
        this.user = user;
        this.flow = flow;
        this.content = content;
        this.timeline = timeline;
    }
}
