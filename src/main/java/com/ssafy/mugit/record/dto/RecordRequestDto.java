package com.ssafy.mugit.record.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordRequestDto {
    private String message;
    private List<Long> sourceIds;
    private List<FilePathDto> filePaths;
}
