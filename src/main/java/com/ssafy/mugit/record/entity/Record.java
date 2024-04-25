package com.ssafy.mugit.record.entity;

import com.ssafy.mugit.flow.main.entity.Flow;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

@Entity(name = "record")
@Getter
@Builder
@AllArgsConstructor
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
    @ColumnDefault("true")
    Boolean isOpen;
}
