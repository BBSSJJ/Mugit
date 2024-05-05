package com.ssafy.mugit.flow.likes.repository;

import com.ssafy.mugit.flow.likes.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    @Query("SELECT l FROM likes l " +
            "LEFT JOIN users u ON l.user.id = u.id " +
            "WHERE l.flow.id = :flowId")
    List<Likes> findAllByFlowId(Long flowId);
}
