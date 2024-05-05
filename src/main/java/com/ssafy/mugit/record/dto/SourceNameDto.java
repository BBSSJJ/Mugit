package com.ssafy.mugit.record.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SourceNameDto {
    Long id;
    String name;
    String sourcePath;
}
