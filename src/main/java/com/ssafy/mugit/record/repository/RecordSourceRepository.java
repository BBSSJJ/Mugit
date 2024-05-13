package com.ssafy.mugit.record.repository;

import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.entity.RecordSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordSourceRepository extends JpaRepository<RecordSource, Long> {
    List<RecordSource> findAllByRecord(Record record);
}
