package com.ssafy.mugit.musitory.entity;

import com.ssafy.mugit.record.entity.Record;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MugitoryRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mugitory_record_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mugitory_id")
    private Mugitory mugitory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    public MugitoryRecord(Mugitory mugitory, Record record) {
        this.mugitory = mugitory;
        this.record = record;
    }
}
