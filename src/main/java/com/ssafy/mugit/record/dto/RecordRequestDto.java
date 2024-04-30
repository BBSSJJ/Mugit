package com.ssafy.mugit.record.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class RecordRequestDto {

    private String message;
    private List<Long> sourceIds;
    private Map<String, String> filePaths;

}
