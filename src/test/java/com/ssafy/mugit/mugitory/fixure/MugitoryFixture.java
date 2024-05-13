package com.ssafy.mugit.mugitory.fixure;

import com.ssafy.mugit.mugitory.entity.Mugitory;
import com.ssafy.mugit.record.entity.Record;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public enum MugitoryFixture {
    MUGITORY_2024_05_09(LocalDate.of(2024, 5, 9), 1),
    MUGITORY_TODAY(LocalDate.now(), 1),
    MUGITORY_YESTERDAY(LocalDate.now().minusDays(1), 1),
    MUGITORY_ONE_YEAR_BEFORE(LocalDate.now().minusYears(1), 1),
    MUGITORY_ONE_YEAR_PLUS_ONE_DAY(LocalDate.now().minusYears(1).plusDays(1), 1),;

    private final LocalDate date;
    private final Integer count;

    public Mugitory getFixture(Record record) {
        return new Mugitory(record, date);
    }
}
