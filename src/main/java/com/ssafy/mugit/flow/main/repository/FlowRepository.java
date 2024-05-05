package com.ssafy.mugit.flow.main.repository;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.repository.querydsl.CustomFlowRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlowRepository extends JpaRepository<Flow, Long>, CustomFlowRepository {
    @Query("SELECT f FROM flow f " +
            "LEFT JOIN users u ON f.user.id = u.id " +
            "LEFT JOIN com.ssafy.mugit.user.entity.Profile p ON p.user.id = u.id " +
            "LEFT JOIN flow_hashtag fh ON f.id = fh.flow.id " +
            "LEFT JOIN hashtag h ON fh.hashtag.id = h.id " +
            "WHERE f.id = :flowId")
    Optional<Flow> findFlowById(Long flowId);

    @Query("SELECT f FROM flow f " +
            "LEFT JOIN users u ON f.user.id = u.id " +
            "LEFT JOIN com.ssafy.mugit.user.entity.Profile p ON p.user.id = u.id " +
            "WHERE f.isReleased = true AND f.authority != 'PRIVATE'")
    List<Flow> findFlows();
}
