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
public class FlowGraphDto {
    Long id;
    String title;
    String musicPath;
    String coverPath;
    List<FlowGraphDto> childFlows = new ArrayList<>();

    public FlowGraphDto(Flow flow) {
        this.id = flow.getId();
        this.title = flow.getTitle();
        this.musicPath = flow.getMusicPath();
        this.coverPath = flow.getCoverPath();
    }
}
