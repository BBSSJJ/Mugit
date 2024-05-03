package com.ssafy.mugit.flow.main.dto.request;

import com.ssafy.mugit.flow.main.entity.Authority;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class RequestCreateNoteDto {
    String title;
    String message;
    List<String> hashtags;
    Authority authority;
    String coverPath;
    String musicPath;
}
