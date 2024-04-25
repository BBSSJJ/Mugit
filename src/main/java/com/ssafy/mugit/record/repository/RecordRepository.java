package com.ssafy.mugit.record.repository;

import com.ssafy.mugit.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    @Query("SELECT r FROM Record r " +
            "JOIN FETCH r.recordSources rs " +
            "JOIN FETCH rs.source s " +
            "WHERE r.id = :recordId")
    public Record findByIdWithSources(Long recordId);

}
