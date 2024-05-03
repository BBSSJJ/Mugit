package com.ssafy.mugit.flow.main.repository;

import com.ssafy.mugit.flow.main.entity.FlowHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowHashtagRepository extends JpaRepository<FlowHashtag, Long> {
}
