package com.ssafy.mugit.record.controller;

import com.ssafy.mugit.global.config.UserSession;
import com.ssafy.mugit.global.dto.MessageDto;
import com.ssafy.mugit.record.dto.RecordRequestDto;
import com.ssafy.mugit.record.dto.RecordResponseDto;
import com.ssafy.mugit.record.service.RecordDeleteService;
import com.ssafy.mugit.record.service.RecordInsertService;
import com.ssafy.mugit.global.dto.UserSessionDto;
import com.ssafy.mugit.record.service.RecordSelectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/records")
@Tag(name = "Record", description = "레코드 생성/삭제/조회에 관련한 API입니다.")
public class RecordController {

    private final RecordInsertService recordInsertService;
    private final RecordDeleteService recordDeleteService;
    private final RecordSelectService recordSelectService;

    @Operation(summary = "레코드 생성", description = "레코드를 생성한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "레코드 생성 완료",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "401", description = "로그인 필요",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 부족",
                    content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @PostMapping("/flows/{flowId}")
    public ResponseEntity<?> createRecord(@PathVariable Long flowId,
                                          @RequestBody RecordRequestDto recordRequestDto,
                                          @UserSession UserSessionDto user) {
        recordInsertService.insertRecord(user.getId(), flowId, recordRequestDto);
        return new ResponseEntity<>(new MessageDto("record create successful"), HttpStatus.OK);
    }

    @Operation(summary = "레코드 조회", description = "레코드를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "레코드 조회 정보",
                    content = @Content(schema = @Schema(implementation = RecordResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "로그인 필요",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 부족",
                    content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @GetMapping("/{recordId}")
    public ResponseEntity<?> findRecord(@PathVariable Long recordId,
                                        @UserSession UserSessionDto user) {
        return new ResponseEntity<>(recordSelectService.selectRecord(user.getId(), recordId), HttpStatus.OK);
    }

    @Operation(summary = "레코드 삭제", description = "레코드를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "레코드 삭제 완료",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "401", description = "로그인 필요",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 부족",
                    content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @DeleteMapping("/{recordId}")
    public ResponseEntity<?> removeRecord(@PathVariable Long recordId,
                                          @UserSession UserSessionDto user) {
        recordDeleteService.deleteRecord(user.getId(), recordId);
        return new ResponseEntity<>(new MessageDto("record delete successful"), HttpStatus.OK);
    }


}
