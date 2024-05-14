package com.ssafy.mugit.feed.controller;

import com.ssafy.mugit.feed.service.FeedService;
import com.ssafy.mugit.flow.main.dto.FlowItemDto;
import com.ssafy.mugit.global.config.UserSession;
import com.ssafy.mugit.global.dto.UserSessionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
@Tag(name = "Feed", description = "피드와 관련한 API입니다.")
public class FeedController {
    private final FeedService feedService;

    @Operation(summary = "Feed 조회", description = "유저가 팔로우하는 유저들의 Flow 조회")
    @GetMapping()
    ResponseEntity<Slice<FlowItemDto>> findFeeds(@UserSession UserSessionDto user,
                                                 @PageableDefault(size = 6, sort = "createdAt", direction = DESC) Pageable pageable) {
        return ResponseEntity.status(200).body(feedService.getFeeds(pageable, user.getId()));
    }
}
