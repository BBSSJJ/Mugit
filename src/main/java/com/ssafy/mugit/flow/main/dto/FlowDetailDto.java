package com.ssafy.mugit.flow.main.dto;

import com.ssafy.mugit.flow.main.entity.Authority;
import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.record.dto.RecordDto;
import com.ssafy.mugit.user.dto.UserProfileDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowDetailDto {
    Long id;
    UserProfileDto user;
    String title;
    String message;
    Authority authority;
    String musicPath;
    String coverPath;
    Integer views;
    Integer likes;
    Boolean likePressed;
    RecordDto record;
    List<String> hashtags = new ArrayList<>();
    String createdAt;

    public FlowDetailDto(Flow flow) {
        this.id = flow.getId();
        this.user = new UserProfileDto(flow.getUser());
        this.title = flow.getTitle();
        this.message = flow.getMessage();
        this.authority = flow.getAuthority();
        this.musicPath = flow.getMusicPath();
        this.coverPath = flow.getCoverPath();
        this.views = flow.getViews();
        //likes 관련 넣자
        this.hashtags = new ArrayList<>();
        flow.getHashtags().forEach(flowHashtag -> {
            this.hashtags.add(flowHashtag.getHashtag().getName());
        });
        this.createdAt = flow.getCreatedAt().toString();

    }

    public void initRecord(RecordDto record) {
        this.record = record;
    }

    public void initLikes(Integer likes, Boolean likePressed) {
        this.likes = likes;
        this.likePressed = likePressed;
    }
}
