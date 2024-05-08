package com.ssafy.mugit.flow.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateReviewDto {
    String content;
    String timeline;
}
