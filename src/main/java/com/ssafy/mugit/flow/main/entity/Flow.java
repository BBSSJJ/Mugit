package com.ssafy.mugit.flow.main.entity;

import com.ssafy.mugit.global.entity.BaseTimeEntity;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "flow")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Flow extends BaseTimeEntity {
    @Transient
    private final String DEFAULT_FLOW_IMAGE_PATH = "https://mugit.site/files/default/flow.png";
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_flow_id")
    private Flow rootFlow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_flow_id")
    private Flow parentFlow;

    @OneToMany(mappedBy = "parentFlow", fetch = FetchType.LAZY)
    private List<Flow> childFlows = new ArrayList<>();


    @OneToMany(mappedBy = "flow", fetch = FetchType.LAZY)
    private List<Record> records = new ArrayList<>();

    @OneToMany(mappedBy = "childFlow", fetch = FetchType.LAZY)
    private List<FlowClosure> relationship = new ArrayList<>();

    @OneToMany(mappedBy = "flow", fetch = FetchType.LAZY)
    private List<FlowHashtag> hashtags = new ArrayList<>();

    public Flow(User user, String title, String musicPath) {
        this.user = user;
        this.title = title;
        this.isReleased = false;
        this.musicPath = musicPath;
        this.views = 0;
    }

    public Flow(User user, String title, String message, Authority authority, String musicPath, String coverPath) {
        this.user = user;
        this.title = title;
        this.message = message;
        this.authority = authority;
        this.isReleased = true;
        this.musicPath = musicPath;
        this.coverPath = !coverPath.isBlank() ? coverPath : DEFAULT_FLOW_IMAGE_PATH;
        this.views = 0;
    }

    public void releaseFlow(String title, String message, Authority authority, String musicPath, String coverPath) {
        this.title = title;
        this.message = message;
        this.authority = authority;
        this.musicPath = musicPath;
        this.coverPath = !coverPath.isBlank() ? coverPath : DEFAULT_FLOW_IMAGE_PATH;
        this.isReleased = true;
    }

    public void initParentAndRoot(Flow rootFlow, Flow parentFlow) {
        this.rootFlow = rootFlow;
        this.parentFlow = parentFlow;
    }

    public void updateViews(Integer views) {
        this.views = views;
    }
}
