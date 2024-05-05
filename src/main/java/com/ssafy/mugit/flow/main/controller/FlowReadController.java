package com.ssafy.mugit.flow.main.controller;

import com.ssafy.mugit.flow.main.dto.FlowDetailDto;
import com.ssafy.mugit.flow.main.dto.FlowItemDto;
import com.ssafy.mugit.flow.main.service.FlowReadService;
import com.ssafy.mugit.global.config.UserSession;
import com.ssafy.mugit.global.dto.ListDto;
import com.ssafy.mugit.global.dto.UserSessionDto;
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
}
