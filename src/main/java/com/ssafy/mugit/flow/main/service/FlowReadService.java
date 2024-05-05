package com.ssafy.mugit.flow.main.service;

import com.ssafy.mugit.flow.likes.entity.Likes;
import com.ssafy.mugit.flow.likes.repository.LikesRepository;
import com.ssafy.mugit.flow.main.dto.FlowDetailDto;
import com.ssafy.mugit.flow.main.dto.FlowItemDto;
import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.record.dto.RecordDto;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.repository.RecordRepository;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlowReadService {
    private final FlowRepository flowRepository;
    private final RecordRepository recordRepository;
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;

    public FlowDetailDto findFlow(Long userId, Long flowId) {
        User user = null;
        Flow flow = flowRepository.findFlowById(flowId).orElseThrow(/* TODO : 에러 처리 */);
        if (userId != null) {
            user = userRepository.getReferenceById(userId);
        }

        FlowDetailDto flowDto = new FlowDetailDto(flow);
        System.out.println(flowDto);

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

    public List<FlowItemDto> listFlow() {
        List<FlowItemDto> flows = flowRepository.findFlows().stream().map(FlowItemDto::new).toList();

        return flows;
    }
}
