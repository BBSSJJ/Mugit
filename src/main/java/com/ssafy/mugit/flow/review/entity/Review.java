package com.ssafy.mugit.flow.review.entity;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.user.main.entity.User;
import jakarta.persistence.*;

@Entity(name = "review")
public class Review {
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
}
