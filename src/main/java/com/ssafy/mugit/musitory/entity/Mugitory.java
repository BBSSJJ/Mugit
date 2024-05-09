package com.ssafy.mugit.musitory.entity;

import com.ssafy.mugit.global.exception.MugitoryException;
import com.ssafy.mugit.musitory.entity.embedded.UserDate;
import com.ssafy.mugit.record.entity.Record;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ssafy.mugit.global.exception.error.MugitoryError.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mugitory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "musitory_id")
    private Long id;

    @Column(nullable = false)
    private Integer count;
    @Column(nullable = false)

    @Embedded
    private UserDate userDate;

    @Column(nullable = false)
    @OneToMany(mappedBy = "mugitory", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MugitoryRecord> mugitoryRecord = new ArrayList<>();

    public Mugitory(long userId, Record record) {
        this.userDate = new UserDate(userId);
        this.count = 1;
        this.mugitoryRecord.add(new MugitoryRecord(this, record));
    }

    public Mugitory(Long id, Long userId, LocalDate now, Integer count, Record record) {
        this.id = id;
        this.userDate = new UserDate(userId, now);
        this.count = count;
        this.mugitoryRecord.add(new MugitoryRecord(this, record));
    }

    public void addRecord(Record record) throws MugitoryException {
        // 기존 등록된 레코드 처리
        for(MugitoryRecord mr : mugitoryRecord) {
            if (mr.getRecord().equals(record)) throw new MugitoryException(ALREADY_RECORDED_TO_MUGITORY);
        };
        // 레코드 추가
        mugitoryRecord.add(new MugitoryRecord(this, record));
        this.count++;
    }

    public void deleteRecord(Record record) throws MugitoryException {
        // 등록되지 않은 레코드 처리
        mugitoryRecord.stream().filter(mr -> mr.getRecord().equals(record)).findAny()
                .orElseThrow(() -> new MugitoryException(DELETE_RECORD_NOT_IN_MUGITORY));

        // 사이즈 1인 뮤지토리 처리
        if (mugitoryRecord.size() == 1) throw new MugitoryException(DELETE_ALL_RECORD_IN_MUGITORY);

        // 삭제
        mugitoryRecord.removeIf(mr -> mr.getRecord().equals(record));
        this.count--;
    }
}
