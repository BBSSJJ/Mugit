package com.ssafy.mugit.record.source.entity;

import com.ssafy.mugit.record.main.entity.Record;
import jakarta.persistence.*;

@Entity(name = "source")
public class Source {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "source_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    Record record;

    @Column(name = "source_path")
    String sourcePath;

    @Column(name = "source_name")
    String name;
}
