package com.ssafy.mugit.flow.main.controller;

import com.ssafy.mugit.auth.SessionKeys;
import com.ssafy.mugit.flow.main.dto.request.RequestCreateNoteDto;
import com.ssafy.mugit.flow.main.dto.request.RequestRegistFlowDto;
import com.ssafy.mugit.flow.main.dto.request.RequestReleaseFlowDto;
import com.ssafy.mugit.flow.main.service.FlowService;
import com.ssafy.mugit.global.web.dto.MessageDto;
import com.ssafy.mugit.user.dto.UserSessionDto;
import jakarta.servlet.http.HttpSession;
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
            HttpSession session, @RequestBody RequestCreateNoteDto requestCreateNoteDto) {
        UserSessionDto userInSession = (UserSessionDto) session.getAttribute(SessionKeys.LOGIN_USER_KEY.getKey());
        Long userId = userInSession.getId();
        flowService.create(userId, requestCreateNoteDto);
        return ResponseEntity.status(201).body(new MessageDto("Note 생성 성공"));
    }

    @PostMapping()
    public ResponseEntity<MessageDto> registFlow(
            HttpSession session, RequestRegistFlowDto requestRegistFlowDto) {
        UserSessionDto userInSession = (UserSessionDto) session.getAttribute(SessionKeys.LOGIN_USER_KEY.getKey());
        Long userId = userInSession.getId();
        flowService.regist(userId, requestRegistFlowDto);
        return ResponseEntity.status(201).body(new MessageDto("Flow 생성 성공"));
    }

    //릴리즈 시 부모의 플로우와 같은 경우에는 못하게 합시다..?
    @PatchMapping("/{flowId}")
    public ResponseEntity<MessageDto> releaseFlow(HttpSession session, RequestReleaseFlowDto requestReleaseFlowDto) {
        UserSessionDto userInSession = (UserSessionDto) session.getAttribute(SessionKeys.LOGIN_USER_KEY.getKey());
        Long userId = userInSession.getId();
        flowService.release(userId, requestReleaseFlowDto);
        return ResponseEntity.status(200).body(new MessageDto("Flow 릴리즈 성공"));
    }
}
