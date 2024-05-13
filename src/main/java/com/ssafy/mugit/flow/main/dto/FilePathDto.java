package com.ssafy.mugit.flow.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilePathDto {
    private String type;
    private String name;
    private String path;
}
