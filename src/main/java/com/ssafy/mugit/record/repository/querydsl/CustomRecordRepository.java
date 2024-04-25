package com.ssafy.mugit.record.repository.querydsl;

import com.ssafy.mugit.record.entity.Record;

public interface CustomRecordRepository {
    Record findByIdWithSources(Long recordId);

}
