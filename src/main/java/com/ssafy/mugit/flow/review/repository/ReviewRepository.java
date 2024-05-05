package com.ssafy.mugit.flow.review.repository;

import com.ssafy.mugit.flow.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM review r " +
            "WHERE r.id = :reviewId AND r.user.id = :userId")
    Optional<Review> findReviewByIdAndUserId(Long reviewId, Long userId);

    @Query("SELECT r FROM review r " +
            "LEFT JOIN users u ON r.user.id = u.id " +
            "WHERE r.flow.id = :flowId " +
            "ORDER BY r.createdAt")
    List<Review> findReviewByFlowId(Long flowId);
}
