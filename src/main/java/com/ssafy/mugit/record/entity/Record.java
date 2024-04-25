package com.ssafy.mugit.record.entity;

import com.ssafy.mugit.flow.main.entity.Flow;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "record")
@Getter
@Builder
@AllArgsConstructor
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flow_id")
    private Flow flow;

    @Column(name = "message")
    private String message;

    @Column(name = "is_open")
    @ColumnDefault("true")
    private Boolean isOpen;

    @OneToMany(mappedBy = "record", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<RecordSource> recordSources;
}
