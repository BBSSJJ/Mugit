package com.ssafy.mugit.flow.main.entity;

import com.ssafy.mugit.hashtag.entity.Hashtag;
import jakarta.persistence.*;

@Entity(name = "flow_hashtag")
public class FlowHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flow_hashtag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flow_id")
    private Flow flow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    public FlowHashtag(Flow flow, Hashtag hashtag) {
        this.flow = flow;
        this.hashtag = hashtag;
    }
}
