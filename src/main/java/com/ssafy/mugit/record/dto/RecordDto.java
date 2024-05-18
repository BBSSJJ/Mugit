package com.ssafy.mugit.record.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.entity.RecordSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDto {
    Long id;
    String message;
    Boolean isOpen;
    List<SourceInfoDto> sources = new ArrayList<>();

    public RecordDto(Record record) {
        this.id = record.getId();
        this.message = record.getMessage();
        this.isOpen = record.getIsOpen();
        if (record.getRecordSources() != null) this.sources = record.getRecordSources().stream().map(recordSource -> new SourceInfoDto(
                                recordSource.getSource().getId(),
                                recordSource.getName(),
                                recordSource.getSource().getPath())).toList();
    }
}
