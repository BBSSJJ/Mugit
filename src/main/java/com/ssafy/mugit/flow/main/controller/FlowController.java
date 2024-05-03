package com.ssafy.mugit.flow.main.controller;

import com.ssafy.mugit.flow.main.dto.FlowGraphDto;
import com.ssafy.mugit.flow.main.dto.request.RequestCreateNoteDto;
import com.ssafy.mugit.flow.main.dto.request.RequestRegistFlowDto;
import com.ssafy.mugit.flow.main.dto.request.RequestReleaseFlowDto;
import com.ssafy.mugit.flow.main.service.FlowService;
import com.ssafy.mugit.global.config.UserSession;
import com.ssafy.mugit.global.dto.MessageDto;
import com.ssafy.mugit.user.dto.UserSessionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flows")
@RequiredArgsConstructor
public class FlowController {
    private final FlowService flowService;

    @PostMapping("/note")
    public ResponseEntity<MessageDto> createNote(
            @UserSession UserSessionDto user, @RequestBody RequestCreateNoteDto requestCreateNoteDto) {
        flowService.create(user.getId(), requestCreateNoteDto);
        return ResponseEntity.status(201).body(new MessageDto("Note 생성 성공"));
    }

    @PostMapping()
    public ResponseEntity<MessageDto> registFlow(
            @UserSession UserSessionDto user, RequestRegistFlowDto requestRegistFlowDto) {
        flowService.regist(user.getId(), requestRegistFlowDto);
        return ResponseEntity.status(201).body(new MessageDto("Flow 생성 성공"));
    }

    //릴리즈 시 부모의 플로우와 같은 경우에는 못하게 합시다..?
    @PatchMapping("/{flowId}")
    public ResponseEntity<MessageDto> releaseFlow(@UserSession UserSessionDto user, RequestReleaseFlowDto requestReleaseFlowDto) {
        flowService.release(user.getId(), requestReleaseFlowDto);
        return ResponseEntity.status(200).body(new MessageDto("Flow 릴리즈 성공"));
    }

    @GetMapping("/api/flows/graph/{flowId}")
    public ResponseEntity<FlowGraphDto> getGraph(@PathVariable("flowId") Long flowId) {
        return ResponseEntity.status(200).body(flowService.graph(flowId));
    }
}
