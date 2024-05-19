package com.ssafy.mugit.flow.main.dto;

import com.ssafy.mugit.flow.main.entity.Flow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowGraphTmpDto {
    Long id;
    String name;
    String image;
    List<FlowGraphTmpDto> childFlows = new ArrayList<>();

    public FlowGraphTmpDto(Flow flow) {
        this.id = flow.getId();
        this.name = flow.getTitle();
        this.image = flow.getCoverPath();
        this.childFlows = flow.getChildFlows().stream().filter(Flow::isReleased).map(FlowGraphTmpDto::new).toList();
    }
}
