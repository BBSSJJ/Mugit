package com.ssafy.mugit.flow.main.repository;

import com.ssafy.mugit.flow.main.entity.Flow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowRepository extends JpaRepository<Flow, Long> {
}
