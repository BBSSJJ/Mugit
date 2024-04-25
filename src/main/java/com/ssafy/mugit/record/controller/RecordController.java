package com.ssafy.mugit.record.controller;

import com.ssafy.mugit.global.web.dto.MessageDto;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/flows/{flowId}")
    public ResponseEntity<?> createRecord(@PathVariable Long flowId,
                                          @RequestParam String message,
                                          @RequestParam List<Long> sourceIds,
                                          @RequestParam Map<String, String> filePaths) {

        recordService.insertRecord(flowId, message, sourceIds, filePaths);

        return new ResponseEntity<>(new MessageDto("record create successful"), HttpStatus.OK);
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<?> findRecord(@PathVariable Long recordId) {
        return new ResponseEntity<>(recordService.selectRecord(recordId), HttpStatus.OK);
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<?> removeRecord(@PathVariable Long recordId) {
        String message = recordService.deleteRecord(recordId);
        return new ResponseEntity<>(new MessageDto(message), HttpStatus.OK);
    }

}
