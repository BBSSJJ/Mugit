package com.ssafy.mugit.record.main.entity;

import com.ssafy.mugit.flow.main.entity.Flow;
import jakarta.persistence.*;

@Entity(name = "record")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flow_id")
    Flow flow;

    @Column(name = "message")
    String message;

    @Column(name = "is_open")
    Boolean isOpen;

}
