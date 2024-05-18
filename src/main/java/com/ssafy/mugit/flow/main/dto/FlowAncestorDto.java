package com.ssafy.mugit.flow.main.dto;

import com.ssafy.mugit.flow.main.entity.Flow;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FlowAncestorDto {
    Long id;
    FlowAncestorDto parentFlow;

    public FlowAncestorDto(Flow flow) {
        this.id = flow.getId();
        this.parentFlow = flow.getParentFlow() != null ? new FlowAncestorDto(flow.getParentFlow()) : null;
    }
}
