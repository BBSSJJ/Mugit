package com.ssafy.mugit.mugitory.dto.response;


import com.querydsl.core.annotations.QueryProjection;
import com.ssafy.mugit.record.entity.Record;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseMugitoryRecordDto {
    private Long id;
    private String message;
    private Boolean isOpen;

    @QueryProjection
    public ResponseMugitoryRecordDto(Record record) {
        this.id = record.getId();
        this.message = record.getMessage();
        this.isOpen = record.getIsOpen();
    }
}
