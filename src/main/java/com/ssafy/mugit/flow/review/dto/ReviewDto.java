package com.ssafy.mugit.flow.review.dto;

import com.ssafy.mugit.flow.review.entity.Review;
import com.ssafy.mugit.user.dto.UserProfileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    Long id;
    UserProfileDto user;
    String content;
    String timeline;

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.user = new UserProfileDto(review.getUser());
        this.content = review.getContent();
        this.timeline = review.getTimeline();
    }
}
