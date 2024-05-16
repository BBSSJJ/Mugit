package com.ssafy.mugit.mugitory.entity;

import com.ssafy.mugit.global.exception.MugitoryException;
import com.ssafy.mugit.global.exception.error.MugitoryError;
import com.ssafy.mugit.mugitory.entity.embedded.UserDate;
import com.ssafy.mugit.record.entity.Record;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mugitory",
        indexes = {@Index(name = "user_date_id_index", columnList = "date, user_id", unique = true)})
public class Mugitory {

    @EmbeddedId
    private UserDate userDateId;

    @Column(nullable = false)
    private Integer count = 0;

    @Column(nullable = false)
    @OneToMany(mappedBy = "mugitory", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Record> records = new ArrayList<>();

    public Mugitory(Record record) {
        this.userDateId = new UserDate(record.getFlow().getUser().getId());
        addRecord(record);
    }

    public Mugitory(Record record, LocalDate date) {
        this.userDateId = new UserDate(record.getFlow().getUser().getId(), date);
        addRecord(record);
    }

    public void addRecord(Record record) {
        records.add(record);
        record.addMugitory(this);
        this.count++;
    }

    public void deleteRecord(Record record) throws MugitoryException {
        // 삭제
        this.count--;
        if (count == 0) throw new MugitoryException(MugitoryError.DELETE_ALL_RECORD_IN_MUGITORY);
    }
}
