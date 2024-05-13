package com.ssafy.mugit.mugitory.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.ssafy.mugit.mugitory.entity.Mugitory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseMugitoryDto {
    private String date;
    private Integer count;

    @QueryProjection
    public ResponseMugitoryDto(Mugitory mugitory) {
        this.count = mugitory.getCount();
        this.date = mugitory.getUserDateId().getDate().toString();
    }
}
