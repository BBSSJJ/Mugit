package com.ssafy.mugit.flow.main.controller;

import com.ssafy.mugit.flow.main.dto.FlowDetailDto;
import com.ssafy.mugit.flow.main.dto.FlowGraphTmpDto;
import com.ssafy.mugit.flow.main.dto.FlowItemDto;
import com.ssafy.mugit.flow.main.service.FlowReadService;
import com.ssafy.mugit.flow.util.FlowCookieUtil;
import com.ssafy.mugit.global.config.UserSession;
import com.ssafy.mugit.global.dto.ListDto;
import com.ssafy.mugit.global.dto.UserSessionDto;
import com.ssafy.mugit.record.dto.RecordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/flows")
@RequiredArgsConstructor
@Tag(name = "Flow(Read)", description = "플로우 조회와 관련한 API입니다.")
public class FlowReadController {

    private final FlowReadService flowReadService;
    private final FlowCookieUtil flowCookieUtil;

    @Operation(summary = "Flow 상세 조회", description = "Flow 상세 정보를 조회")
    @GetMapping("/{flowId}")
    ResponseEntity<FlowDetailDto> getFlow(
            HttpServletRequest request,
            HttpServletResponse response,
            @UserSession UserSessionDto user,
            @PathVariable Long flowId) {
        return ResponseEntity.status(200).body(flowReadService.findFlow(user.getId(), flowId, flowCookieUtil.firstRead(request, response, flowId)));
    }

    @Operation(summary = "전체 Flow", description = "기본값으로 size=12, sort='createdAt'으로 되어있으므로 page 변수만 보내면 됩니다.")
    @GetMapping()
    ResponseEntity<Slice<FlowItemDto>> getFlowList(@PageableDefault(size = 12, sort = "createdAt", direction = DESC) Pageable pageable) {
        return ResponseEntity.status(200).body(flowReadService.listFlow(pageable));
    }

    @Operation(summary = "내 Flow 리스트 조회", description = "릴리즈한 내 Flow List 조회")
    @GetMapping("/mine")
    ResponseEntity<ListDto<List<FlowItemDto>>> getMyFlowList(@UserSession UserSessionDto user) {
        return ResponseEntity.status(200).body(new ListDto<>(flowReadService.listMyFlow(user.getId())));
    }

    @Operation(summary = "작업중인 Flow 리스트 조회", description = "아직 릴리즈 하지 않은 내 Flow List 조회")
    @GetMapping("/working")
    ResponseEntity<ListDto<List<FlowItemDto>>> getMyWorkingFlowList(@UserSession UserSessionDto user) {
        return ResponseEntity.status(200).body(new ListDto<>(flowReadService.listMyWorkingFlow(user.getId())));
    }

    @Operation(summary = "좋아요 Flow 리스트 조회", description = "내가 좋아요 누른 Flow List 조회")
    @GetMapping("/likes")
    ResponseEntity<ListDto<List<FlowItemDto>>> getMyLikeFlowList(@UserSession UserSessionDto user) {
        return ResponseEntity.status(200).body(new ListDto<>(flowReadService.listMyLikeFlow(user.getId())));
    }

    @Operation(summary = "Flow 의 Record 조회", description = "Flow의 Record들 조회")
    @GetMapping("/{flowId}/records")
    ResponseEntity<ListDto<List<RecordDto>>> getFlowRecords(@PathVariable("flowId") Long flowId) {
        return ResponseEntity.status(200).body(new ListDto<>(flowReadService.listFlowRecords(flowId)));
    }

    @Operation(summary = "Flow 그래프 조회", description = "Flow가 속한 그래프 전체 조회")
    @GetMapping("/{flowId}/graph")
    ResponseEntity<FlowGraphTmpDto> getFlowGraph(@PathVariable("flowId") Long flowId) {
        return ResponseEntity.status(200).body(flowReadService.getFlowGraph(flowId));
    }

    @Operation(summary = "Flow 장르 검색", description = "장르 이름 검색. 기본값으로 size=12, sort='createdAt'으로 되어있으므로 page 변수만 보내면 됩니다.")
    @GetMapping("/genre")
    ResponseEntity<Slice<FlowItemDto>> searchFlowsByGenre(@PageableDefault(size = 12, sort = "createdAt", direction = DESC) Pageable pageable,
                                                          @RequestParam("hashtag") String hashtag) {
        return ResponseEntity.status(200).body(flowReadService.getFlowsByGenre(pageable, hashtag));
    }

    @Operation(summary = "Flow 검색", description = "키워드로 제목, 해시태그, 유저 검색. 기본값으로 size=12, sort='createdAt'으로 되어있으므로 page 변수만 보내면 됩니다.")
    @GetMapping("/search")
    ResponseEntity<Slice<FlowItemDto>> searchFlowsByKeyword(@PageableDefault(size = 12, sort = "createdAt", direction = DESC) Pageable pageable,
                                                            @RequestParam("keyword") String keyword) {
        return ResponseEntity.status(200).body(flowReadService.getFlowsByKeyword(pageable, keyword));
    }

    @Operation(summary = "Flow 조상 조회", description = "플로우의 조상 모두 조회.")
    @GetMapping("/{flowId}/ancestors")
    ResponseEntity<ListDto<List<FlowItemDto>>> getAncestorFlows(@PathVariable("flowId") Long flowId) {
        return ResponseEntity.status(200).body(new ListDto<>(flowReadService.listAncestorFlow(flowId)));
    }

}
