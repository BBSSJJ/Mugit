package com.ssafy.mugit.flow.main.service;

import com.ssafy.mugit.flow.likes.entity.Likes;
import com.ssafy.mugit.flow.likes.repository.LikesRepository;
import com.ssafy.mugit.flow.main.dto.FlowAncestorDto;
import com.ssafy.mugit.flow.main.dto.FlowDetailDto;
import com.ssafy.mugit.flow.main.dto.FlowGraphTmpDto;
import com.ssafy.mugit.flow.main.dto.FlowItemDto;
import com.ssafy.mugit.flow.main.entity.Authority;
import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.record.dto.RecordDto;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.repository.RecordRepository;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlowReadService {
    private final FlowRepository flowRepository;
    private final RecordRepository recordRepository;
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;

    @Transactional
    public FlowDetailDto findFlow(Long userId, Long flowId, Boolean firstRead) {
        User user = null;
        Flow flow = flowRepository.findFlowById(flowId).orElseThrow(/* TODO : 에러 처리 */);
        if (userId != null) {
            user = userRepository.getReferenceById(userId);
        }

        // 조회수 증가, 쿠키 생성
        if (firstRead) {
            flow.updateViews(flow.getViews() + 1);
        }

        FlowDetailDto flowDto = new FlowDetailDto(flow);

        // 권한 검사

        // Record 조회
        Record record = recordRepository.findLastRecordByFlowId(flowId).orElseThrow(/* TODO : 에러 처리 */);
        flowDto.initRecord(new RecordDto(record));

        // Like 조회
        List<Likes> likesList = likesRepository.findAllByFlowId(flowId);
        boolean likePressed = false;
        if (user != null) {
            for (Likes likes : likesList) {
                if (likes.getUser().equals(user)) {
                    likePressed = true;
                    break;
                }
            }
        }
        flowDto.initLikes(likesList.size(), likePressed);

        return flowDto;
    }

    @Transactional(readOnly = true)
    public Slice<FlowItemDto> listFlow(Pageable pageable) {
        return flowRepository.findAll(pageable).map(FlowItemDto::new);
    }

    public List<FlowItemDto> listMyFlow(Long userId) {
        User user = userRepository.getReferenceById(userId);
        return flowRepository.findFlowsByUserId(user.getId()).stream().map(FlowItemDto::new).toList();
    }

    public List<FlowItemDto> listMyWorkingFlow(Long userId) {
        User user = userRepository.getReferenceById(userId);
        return flowRepository.findWorkingFlowsByUserId(user.getId()).stream().map(FlowItemDto::new).toList();
    }

    public List<FlowItemDto> listMyLikeFlow(Long userId) {
        User user = userRepository.getReferenceById(userId);
        return flowRepository.findMyLikeFlows(user.getId()).stream().map(FlowItemDto::new).toList();
    }

    public List<RecordDto> listFlowRecords(Long flowId) {
        Flow flow = flowRepository.getReferenceById(flowId);
        if (flow.getAuthority() == Authority.PRIVATE) {
            /* TODO : 에러 처리 */
        }
        return recordRepository.findRecordsByFlowId(flowId).stream().map(RecordDto::new).toList();
    }

    public FlowGraphTmpDto getFlowGraph(Long flowId) {
        Flow flow = flowRepository.getReferenceById(flowId);
        Flow rootFlow = flow.getRootFlow();
        flowRepository.findFlowsByRootFlow(rootFlow);

        return new FlowGraphTmpDto(rootFlow);
    }

    public Slice<FlowItemDto> getFlowsByGenre(Pageable pageable, String hashtag) {
        return flowRepository.findFlowsByHashtag(pageable, hashtag).map(FlowItemDto::new);
    }

    public Slice<FlowItemDto> getFlowsByKeyword(Pageable pageable, String keyword) {
        return flowRepository.findFlowsByKeyword(pageable, keyword).map(FlowItemDto::new);
    }

    public List<FlowItemDto> listAncestorFlow(Long flowId) {
        List<FlowItemDto> flowList = new ArrayList<>();
        Flow flow = flowRepository.getReferenceById(flowId);

        FlowAncestorDto tmp = new FlowAncestorDto(flow);
        while (tmp.getParentFlow() != null) {
            tmp = tmp.getParentFlow();
            flowList.add(new FlowItemDto(flowRepository.getReferenceById(tmp.getId())));
        }

        return flowList;
    }
}
