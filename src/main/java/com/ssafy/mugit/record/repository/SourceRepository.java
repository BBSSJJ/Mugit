package com.ssafy.mugit.record.repository;

import com.ssafy.mugit.record.entity.Source;
import com.ssafy.mugit.record.repository.querydsl.CustomSourceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepository extends JpaRepository<Source, Long>, CustomSourceRepository {
}
