package com.ssafy.mugit.flow.main.repository;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.repository.querydsl.CustomFlowRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlowRepository extends JpaRepository<Flow, Long>, CustomFlowRepository {
    @Query("SELECT f FROM flow f " +
            "LEFT JOIN FETCH f.user u " +
            "LEFT JOIN FETCH u.profile " +
            "LEFT JOIN FETCH f.hashtags h " +
            "LEFT JOIN FETCH h.hashtag " +
            "WHERE f.id = :flowId")
    Optional<Flow> findFlowById(Long flowId);

    @Query("SELECT DISTINCT f FROM flow f " +
            "LEFT JOIN FETCH f.user u " +
            "LEFT JOIN FETCH u.profile " +
            "WHERE f.isReleased = true AND f.authority != 'PRIVATE'")
    Page<Flow> findAll(Pageable pageable);

    @Query("SELECT f FROM flow f " +
            "LEFT JOIN FETCH f.user u " +
            "LEFT JOIN FETCH u.profile " +
            "WHERE f.isReleased = true AND f.user.id = :userId")
    List<Flow> findAllByUserId(Long userId);

    @Query("SELECT f FROM flow f " +
            "LEFT JOIN FETCH f.user u " +
            "LEFT JOIN FETCH u.profile " +
            "WHERE f.isReleased = true AND f.user.id = :userId AND f.authority != 'PRIVATE'")
    List<Flow> findFlowsByUserId(Long userId);

    @Query("SELECT f FROM flow f " +
            "LEFT JOIN FETCH f.user u " +
            "LEFT JOIN FETCH u.profile " +
            "WHERE f.isReleased = false AND f.user.id = :userId")
    List<Flow> findUnreleasedFlowsByUserId(Long userId);

    @Query("SELECT f FROM likes l " +
            "LEFT JOIN flow f ON l.flow.id = flow.id " +
            "LEFT JOIN FETCH f.user u " +
            "LEFT JOIN FETCH u.profile " +
            "WHERE l.user.id = :userId AND f.authority != 'PRIVATE'")
    List<Flow> findLikeFlows(Long userId);

    List<Flow> findFlowsByRootFlow(Flow rootFlow);

    @Query("SELECT DISTINCT f FROM flow f " +
            "LEFT JOIN FETCH f.user u " +
            "LEFT JOIN FETCH u.profile " +
            "LEFT JOIN FETCH f.hashtags ht " +
            "LEFT JOIN FETCH ht.hashtag h " +
            "WHERE h.name = :hashtag")
    Page<Flow> findFlowsByHashtag(Pageable pageable, String hashtag);

    @Query("SELECT DISTINCT f FROM flow f " +
            "LEFT JOIN FETCH f.user u " +
            "LEFT JOIN FETCH u.profile p " +
            "LEFT JOIN FETCH f.hashtags ht " +
            "LEFT JOIN FETCH ht.hashtag h " +
            "WHERE p.nickName LIKE %:keyword% " +
            "OR h.name LIKE %:keyword% " +
            "OR f.title LIKE %:keyword%")
    Page<Flow> findFlowsByKeyword(Pageable pageable, String keyword);
}
