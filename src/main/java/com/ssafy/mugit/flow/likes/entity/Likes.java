package com.ssafy.mugit.flow.likes.entity;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.user.entity.User;
import jakarta.persistence.*;

@Entity(name = "likes")
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flow_id")
    Flow flow;
}
