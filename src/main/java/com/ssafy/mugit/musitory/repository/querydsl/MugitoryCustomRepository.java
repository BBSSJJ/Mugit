package com.ssafy.mugit.musitory.repository.querydsl;

import com.ssafy.mugit.musitory.entity.MugitoryRecord;

public interface MugitoryCustomRepository {
    MugitoryRecord findByIdWithMugitoryRecord(Long id);
}
