package com.ssafy.mugit.flow.main.controller;

import com.ssafy.mugit.flow.main.dto.FlowDetailDto;
import com.ssafy.mugit.flow.main.dto.FlowGraphTmpDto;
import com.ssafy.mugit.flow.main.dto.FlowItemDto;
import com.ssafy.mugit.flow.main.service.FlowReadService;
import com.ssafy.mugit.global.config.UserSession;
import com.ssafy.mugit.global.dto.ListDto;
import com.ssafy.mugit.global.dto.UserSessionDto;
import com.ssafy.mugit.record.dto.RecordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/flows")
@RequiredArgsConstructor
public class FlowReadController {

    private final FlowReadService flowReadService;

    @GetMapping("/{flowId}")
    ResponseEntity<FlowDetailDto> getFlow(@UserSession UserSessionDto user, @PathVariable Long flowId) {
        return ResponseEntity.status(200).body(flowReadService.findFlow(user.getId(), flowId));
    }

    @GetMapping()
    ResponseEntity<ListDto<List<FlowItemDto>>> getFlowList() {
        return ResponseEntity.status(200).body(new ListDto<>(flowReadService.listFlow()));
    }

    @GetMapping("/mine")
    ResponseEntity<ListDto<List<FlowItemDto>>> getMyFlowList(@UserSession UserSessionDto user) {
        return ResponseEntity.status(200).body(new ListDto<>(flowReadService.listMyFlow(user.getId())));
    }

    @GetMapping("/working")
    ResponseEntity<ListDto<List<FlowItemDto>>> getMyWorkingFlowList(@UserSession UserSessionDto user) {
        return ResponseEntity.status(200).body(new ListDto<>(flowReadService.listMyWorkingFlow(user.getId())));
    }

    @GetMapping("/likes")
    ResponseEntity<ListDto<List<FlowItemDto>>> getMyLikeFlowList(@UserSession UserSessionDto user) {
        return ResponseEntity.status(200).body(new ListDto<>(flowReadService.listMyLikeFlow(user.getId())));
    }

    @GetMapping("/{flowId}/records")
    ResponseEntity<ListDto<List<RecordDto>>> getFlowRecords(@PathVariable("flowId") Long flowId) {
        return ResponseEntity.status(200).body(new ListDto<>(flowReadService.listFlowRecords(flowId)));
    }

    @GetMapping("/{flowId}/graph")
    ResponseEntity<FlowGraphTmpDto> getFlowGraph(@PathVariable("flowId") Long flowId) {
        return ResponseEntity.status(200).body(flowReadService.getFlowGraph(flowId));
    }
}
