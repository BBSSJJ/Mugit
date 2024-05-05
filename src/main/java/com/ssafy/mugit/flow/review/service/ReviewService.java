package com.ssafy.mugit.flow.review.service;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.flow.review.dto.RequestCreateReviewDto;
import com.ssafy.mugit.flow.review.dto.ReviewDto;
import com.ssafy.mugit.flow.review.entity.Review;
import com.ssafy.mugit.flow.review.repository.ReviewRepository;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FlowRepository flowRepository;

    public void createReview(Long userId, Long flowId, RequestCreateReviewDto requestCreateReviewDto) {
        User user = userRepository.getReferenceById(userId);
        Flow flow = flowRepository.getReferenceById(flowId);
        String content = requestCreateReviewDto.getContent();
        if (content == null) {
            /* TODO : 에러 처리 */
        }
        Review review = new Review(user, flow, requestCreateReviewDto.getContent());
        reviewRepository.save(review);
    }

    public void eraseReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findReviewByIdAndUserId(reviewId, userId).orElseThrow(/* TODO : 에러 처리 */);
        reviewRepository.delete(review);
    }

    public List<ReviewDto> listReview(Long flowId) {
        return reviewRepository.findReviewByFlowId(flowId).stream().map(ReviewDto::new).toList();
    }
}
