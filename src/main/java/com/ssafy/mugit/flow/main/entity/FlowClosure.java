package com.ssafy.mugit.flow.main.entity;

import jakarta.persistence.*;

@Entity(name = "flow_closure")
public class FlowClosure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flow_closure_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_flow_id")
    private Flow parentFlow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_flow_id")
    private Flow childFlow;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_id")
    private Flow rootFlow;

    @Column(name = "depth")
    private Integer depth;
}
