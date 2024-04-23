package com.ssafy.mugit.flow.main.entity;

import com.ssafy.mugit.user.entity.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "flow")
public class Flow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flow_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "title")
    private String title;
    @Column(name = "is_released")
    private boolean isReleased;
    @Column(name = "message")
    private String message;
    @Column(name = "authority")
    @Enumerated(value = EnumType.STRING)
    private Authority authority;
    @Column(name = "music_path")
    private String musicPath;
    @Column(name = "cover_path")
    private String coverPath;
    @Column(name = "views")
    private Integer views;

    @OneToMany(mappedBy = "childFlow", fetch = FetchType.LAZY)
    private List<FlowClosure> relationship = new ArrayList<>();

    @OneToMany(mappedBy = "flow", fetch = FetchType.LAZY)
    private List<FlowHashtag> hashtags = new ArrayList<>();
}
