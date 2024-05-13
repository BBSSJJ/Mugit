package com.ssafy.mugit.mugitory.controller;

import com.ssafy.mugit.global.config.UserSession;
import com.ssafy.mugit.global.dto.ListDto;
import com.ssafy.mugit.global.dto.MessageDto;
import com.ssafy.mugit.global.dto.UserSessionDto;
import com.ssafy.mugit.mugitory.dto.response.ResponseMugitoryDto;
import com.ssafy.mugit.mugitory.dto.response.ResponseMugitoryRecordDto;
import com.ssafy.mugit.mugitory.service.MugitoryService;
import com.ssafy.mugit.record.dto.RecordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mugitories")
@Tag(name = "Mugitory", description = "뮤지토리와 관련한 API입니다.")
@RequiredArgsConstructor
public class MugitoryController {

    private final MugitoryService mugitoryService;

    @Operation(summary = "전체 뮤지토리 조회", description = "모든 뮤지토리를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "뮤지토리 조회 완료", content = @Content(schema = @Schema(implementation = ResponseMugitoryDto.class))),
            @ApiResponse(responseCode = "401", description = "토큰인증 실패", content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @GetMapping
    public ResponseEntity<ListDto<List<ResponseMugitoryDto>>> getMugitory(@UserSession UserSessionDto user) {
        return ResponseEntity.ok(new ListDto<>(mugitoryService.getOneYearMugitoryByUserId(user.getId())));
    }

    @Operation(summary = "특정일 뮤지토리 조회", description = "특정한 날짜의 뮤지토리를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "뮤지토리 조회 완료", content = @Content(schema = @Schema(implementation = RecordDto.class))),
            @ApiResponse(responseCode = "401", description = "토큰인증 실패", content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "404", description = "해당일자 뮤지토리 없음", content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @GetMapping("/{date}")
    public ResponseEntity<ListDto<List<ResponseMugitoryRecordDto>>> getMugitoryByDate(
            @UserSession UserSessionDto user,
            @PathVariable String date) {
        return ResponseEntity.ok(new ListDto<>(mugitoryService.getMugitoryRecordByDate(user.getId(), date)));
    }
}
