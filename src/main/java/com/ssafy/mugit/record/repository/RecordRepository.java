package com.ssafy.mugit.record.repository;

import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.repository.querydsl.CustomRecordRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long>, CustomRecordRepository {
}
