package com.ssafy.mugit.musitory.fixure;

import com.ssafy.mugit.musitory.entity.Mugitory;
import com.ssafy.mugit.record.entity.Record;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public enum MugitoryFixture {
    MUGITORY(1L, 1L, LocalDate.of(2024, 5, 9), 1);

    private final Long id;
    private final Long userId;
    private final LocalDate date;
    private final Integer count;

    public Mugitory getFixture(Record record) {
        return new Mugitory(id, userId, date, count, record);
    }
}
