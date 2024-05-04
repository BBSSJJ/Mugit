package com.ssafy.mugit.record.entity;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "record")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Record extends BaseTimeEntity {
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
    private List<RecordSource> recordSources = new ArrayList<>();

    public Record(Flow flow, String message, Boolean isOpen, List<RecordSource> recordSources) {
        this.flow = flow;
        this.message = message;
        this.isOpen = isOpen;
        this.recordSources = recordSources;
    }

    public Record(Flow flow, String message, Boolean isOpen) {
        this.flow = flow;
        this.message = message;
        this.isOpen = isOpen;
    }

    public void initRecordSources(List<RecordSource> recordSources) {
        this.recordSources = recordSources;
    }
}
