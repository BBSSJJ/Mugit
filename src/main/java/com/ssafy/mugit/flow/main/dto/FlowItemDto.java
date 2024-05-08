package com.ssafy.mugit.flow.main.dto;

import com.ssafy.mugit.flow.main.entity.Authority;
import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.user.dto.UserProfileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowItemDto {
    Long id;
    UserProfileDto user;
    String title;
    Authority authority;
    String musicPath;
    String coverPath;
    String createdAt;
    List<String> hashtags = new ArrayList<>();

    public FlowItemDto(Flow flow) {
        this.id = flow.getId();
        this.user = new UserProfileDto(flow.getUser());
        this.title = flow.getTitle();
        this.authority = flow.getAuthority();
        this.musicPath = flow.getMusicPath();
        this.coverPath = flow.getCoverPath();
        this.createdAt = flow.getCreatedAt().toString();
        this.hashtags = new ArrayList<>();
        flow.getHashtags().forEach(flowHashtag -> {
            this.hashtags.add(flowHashtag.getHashtag().getName());
        });
    }
}
