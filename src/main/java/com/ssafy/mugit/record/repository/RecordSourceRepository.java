package com.ssafy.mugit.record.repository;

import com.ssafy.mugit.record.entity.RecordSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordSourceRepository extends JpaRepository<RecordSource, Long> {
}
