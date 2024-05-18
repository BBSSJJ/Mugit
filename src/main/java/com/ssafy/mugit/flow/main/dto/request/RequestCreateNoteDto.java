package com.ssafy.mugit.flow.main.dto.request;

import com.ssafy.mugit.flow.main.dto.FilePathDto;
import com.ssafy.mugit.flow.main.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateNoteDto {
    String title;
    String message;
    Authority authority;
    List<FilePathDto> files = new ArrayList<>();
    List<String> hashtags = new ArrayList<>();
}
