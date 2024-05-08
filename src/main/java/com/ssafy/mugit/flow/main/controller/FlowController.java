package com.ssafy.mugit.flow.main.controller;

import com.ssafy.mugit.flow.main.dto.request.RequestCreateNoteDto;
import com.ssafy.mugit.flow.main.dto.request.RequestReleaseFlowDto;
import com.ssafy.mugit.flow.main.service.FlowService;
import com.ssafy.mugit.global.config.UserSession;
import com.ssafy.mugit.global.dto.MessageDto;
import com.ssafy.mugit.global.dto.UserSessionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flows")
@RequiredArgsConstructor
@Tag(name = "Flow", description = "플로우와 관련한 API입니다.")
public class FlowController {
    private final FlowService flowService;

    @Operation(summary = "Note 생성", description = "첫 Note를 생성")
    @PostMapping("/note")
    public ResponseEntity<MessageDto> createNote(
            @UserSession UserSessionDto user, @RequestBody RequestCreateNoteDto requestCreateNoteDto) {
        flowService.create(user.getId(), requestCreateNoteDto);
        return ResponseEntity.status(201).body(new MessageDto("Note 생성 성공"));
    }

    @Operation(summary = "Flow 생성", description = "부모 Flow로부터 새로운 Flow 생성")
    @PostMapping("/parent/{parentId}")
    public ResponseEntity<MessageDto> registFlow(
            @UserSession UserSessionDto user, @PathVariable("parentId") Long parentId) {
        flowService.regist(user.getId(), parentId);
        return ResponseEntity.status(201).body(new MessageDto("Flow 생성 성공"));
    }

    //릴리즈 시 부모의 플로우와 같은 경우에는 못하게 합시다..?
    @Operation(summary = "Flow 릴리즈", description = "Flow 릴리즈, files에는 file-server로부터 받아온 응답 그대로 넣어서 보내면 됩니다.")
    @PatchMapping("/{flowId}")
    public ResponseEntity<MessageDto> releaseFlow(@UserSession UserSessionDto user, @PathVariable("flowId") Long flowId, @RequestBody RequestReleaseFlowDto requestReleaseFlowDto) {
        flowService.release(user.getId(), flowId, requestReleaseFlowDto);
        return ResponseEntity.status(200).body(new MessageDto("Flow 릴리즈 성공"));
    }
}
