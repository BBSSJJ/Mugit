package com.ssafy.mugit.notification.controller;

import com.ssafy.mugit.global.config.UserSession;
import com.ssafy.mugit.global.dto.ListDto;
import com.ssafy.mugit.global.dto.MessageDto;
import com.ssafy.mugit.global.dto.UserSessionDto;
import com.ssafy.mugit.global.dto.NotificationDto;
import com.ssafy.mugit.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "일림과 관련한 API입니다.")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 조회", description = "읽지 않은 전체 알림을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 조회완료", content = @Content(schema = @Schema(implementation = NotificationDto.class))),
            @ApiResponse(responseCode = "401", description = "Security Filter 통과 실패 : 로그인 정보 없음", content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "404", description = "안읽은 알림 없음", content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @GetMapping
    public ResponseEntity<ListDto<List<NotificationDto>>> getNotifications(@UserSession UserSessionDto user) {
        List<NotificationDto> allNotifications = notificationService.findAllNotifications(user.getId());
        return ResponseEntity.ok().body(new ListDto<List<NotificationDto>> (allNotifications));
    }

    @Operation(summary = "알림 읽기", description = "해당 알림을 읽는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 읽기완료", content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "401", description = "Security Filter 통과 실패 : 로그인 정보 없음", content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 알림 없음", content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @PatchMapping("/{notificationId}")
    public ResponseEntity<MessageDto> read(@PathVariable Long notificationId, @UserSession UserSessionDto user) {
        notificationService.read(notificationId, user.getId());
        return ResponseEntity.ok().body(new MessageDto("알림 읽기완료"));
    }

    @Operation(summary = "전체알림 읽기", description = "전체 알림을 읽는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 읽기완료", content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "401", description = "Security Filter 통과 실패 : 로그인 정보 없음", content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 알림 없음", content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @PatchMapping("")
    public ResponseEntity<MessageDto> readAll(@UserSession UserSessionDto user) {
        notificationService.readAll(user.getId());
        return ResponseEntity.ok().body(new MessageDto("전체 알림 읽기완료"));
    }
}
