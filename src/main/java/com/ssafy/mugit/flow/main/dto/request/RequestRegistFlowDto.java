package com.ssafy.mugit.flow.main.dto.request;

import com.ssafy.mugit.flow.main.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class RequestRegistFlowDto {
    Long parentFlowId;
    String title;
    String message;
    Authority authority;
    String coverPath;
}
