package com.ssafy.mugit.record.repository;

import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.repository.querydsl.CustomRecordRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long>, CustomRecordRepository {
    @Query("SELECT r " +
            "FROM record r " +
            "LEFT JOIN record_source rs ON r.id = rs.record.id " +
            "LEFT JOIN source s ON rs.source.id = s.id " +
            "WHERE r.flow.id = :flowId " +
            "ORDER BY r.createdAt DESC " +
            "LIMIT 1")
    Optional<Record> findLastRecordByFlowId(Long flowId);

    @Query("SELECT r " +
            "FROM record r " +
            "LEFT JOIN record_source rs ON r.id = rs.record.id " +
            "LEFT JOIN source s ON rs.source.id = s.id " +
            "WHERE r.flow.id = :flowId " +
            "ORDER BY r.createdAt")
    List<Record> findRecordsByFlowId(Long flowId);
}
