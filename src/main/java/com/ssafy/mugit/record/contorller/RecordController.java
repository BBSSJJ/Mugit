package com.ssafy.mugit.record.contorller;

import com.ssafy.mugit.global.dto.MessageDto;
import com.ssafy.mugit.record.service.RecordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file/records")
@Slf4j
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/{flowId}")
    public ResponseEntity<?> createRecord(HttpServletRequest request,
                                          @PathVariable Long flowId,
                                          @RequestParam("message") String message,
                                          @RequestParam("sourceIds") List<Long> sourceIds,
                                          @RequestParam(value = "files", required = false) List<MultipartFile> files) {

        String response = recordService.fileUpload(request, flowId, message, sourceIds, files);

        return new ResponseEntity<>(new MessageDto(response), HttpStatus.OK);
    }
}