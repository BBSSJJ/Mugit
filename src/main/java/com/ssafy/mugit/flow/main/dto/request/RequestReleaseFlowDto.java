package com.ssafy.mugit.flow.main.dto.request;

import com.ssafy.mugit.flow.main.dto.FilePathDto;
import com.ssafy.mugit.flow.main.entity.Authority;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequestReleaseFlowDto {
    String title;
    String message;
    Authority authority;
    List<FilePathDto> files = new ArrayList<>();
    List<String> hashtags = new ArrayList<>();
}
