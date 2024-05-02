package com.ssafy.mugit.record.controller;

import com.ssafy.mugit.global.config.UserSession;
import com.ssafy.mugit.global.dto.MessageDto;
import com.ssafy.mugit.record.dto.RecordRequestDto;
import com.ssafy.mugit.record.service.RecordService;
import com.ssafy.mugit.user.dto.UserSessionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;

    @GetMapping("/flows/{flowId}")
    public ResponseEntity<?> validateFlowId(@PathVariable Long flowId,
                                            @UserSession UserSessionDto user) {
        recordService.validateFlow(user.getId(), flowId);
        return new ResponseEntity<>(new MessageDto("Flow is validated"), HttpStatus.OK);
    }

    @PostMapping("/flows/{flowId}")
    public ResponseEntity<?> createRecord(@PathVariable Long flowId,
                                          @RequestBody RecordRequestDto recordRequestDto) {
        recordService.insertRecord(flowId, recordRequestDto);
        return new ResponseEntity<>(new MessageDto("record create successful"), HttpStatus.OK);
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<?> findRecord(@PathVariable Long recordId,
                                        @UserSession UserSessionDto user) {
        return new ResponseEntity<>(recordService.selectRecord(user.getId(), recordId), HttpStatus.OK);
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<?> removeRecord(@PathVariable Long recordId,
                                          @UserSession UserSessionDto user) {
        recordService.deleteRecord(user.getId(), recordId);
        return new ResponseEntity<>(new MessageDto("record delete successful"), HttpStatus.OK);
    }


}
