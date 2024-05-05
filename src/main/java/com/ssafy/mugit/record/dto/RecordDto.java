package com.ssafy.mugit.record.dto;

import com.ssafy.mugit.record.entity.Record;
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
    List<SourceNameDto> sources = new ArrayList<>();

    public RecordDto(Record record) {
        this.id = record.getId();
        this.message = record.getMessage();
        this.isOpen = record.getIsOpen();
        this.sources = new ArrayList<>();
        record.getRecordSources().forEach(recordSource -> {
            this.sources.add(new SourceNameDto(recordSource.getSource().getId(),
                    recordSource.getName(),
                    recordSource.getSource().getPath()));
        });
    }
}
