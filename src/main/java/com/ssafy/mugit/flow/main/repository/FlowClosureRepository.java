package com.ssafy.mugit.flow.main.repository;

import com.ssafy.mugit.flow.main.entity.FlowClosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowClosureRepository extends JpaRepository<FlowClosure, Long> {
}
