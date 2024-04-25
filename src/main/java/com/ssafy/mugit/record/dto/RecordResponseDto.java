package com.ssafy.mugit.record.dto;

import com.ssafy.mugit.record.entity.Source;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecordResponseDto {
    private Long id;
    private String message;
    private List<Source> sources;
}
