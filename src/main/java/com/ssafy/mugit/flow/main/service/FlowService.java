package com.ssafy.mugit.flow.main.service;

import com.ssafy.mugit.flow.main.dto.FlowGraphDto;
import com.ssafy.mugit.flow.main.dto.request.RequestCreateNoteDto;
import com.ssafy.mugit.flow.main.dto.request.RequestRegistFlowDto;
import com.ssafy.mugit.flow.main.dto.request.RequestReleaseFlowDto;
import com.ssafy.mugit.flow.main.entity.Authority;
import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.entity.FlowClosure;
import com.ssafy.mugit.flow.main.repository.FlowClosureRepository;
import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.hashtag.entity.Hashtag;
import com.ssafy.mugit.hashtag.service.HashtagService;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.repository.RecordRepository;
import com.ssafy.mugit.record.service.RecordService;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlowService {
    private final FlowRepository flowRepository;
    private final FlowClosureRepository flowClosureRepository;
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final HashtagService hashtagService;
    private final RecordService recordService;
    private final FlowHashtagService flowHashtagService;


    public void create(Long userId, RequestCreateNoteDto requestCreateNoteDto) {
        User user = userRepository.getReferenceById(userId);

        // Flow 생성
        Flow note = new Flow(user,
                requestCreateNoteDto.getTitle(),
                requestCreateNoteDto.getMessage(),
                requestCreateNoteDto.getAuthority(),
                requestCreateNoteDto.getMusicPath(),
                requestCreateNoteDto.getCoverPath());
        flowRepository.save(note);

        // 필요한 Hashtag 목록 추가
        List<Hashtag> hashtags = hashtagService.update(requestCreateNoteDto.getHashtags());

        // Flow Hashtag 연결테이블 생성
        flowHashtagService.addHashtags(note, hashtags);

        // Flow Closure 테이블에 추가
        flowClosureRepository.save(new FlowClosure(note, note, note, 1));

        // Record 생성
    }

    public void regist(Long userId, RequestRegistFlowDto requestRegistFlowDto) {

        User user = userRepository.getReferenceById(userId);
        Flow parentFlow = flowRepository.getReferenceById(requestRegistFlowDto.getParentFlowId());

        // 권한 검사
        if ((parentFlow.getAuthority() == Authority.PROTECTED && !parentFlow.getId().equals(userId)) ||
                parentFlow.getAuthority() == Authority.PRIVATE) {
            /* TODO: 에러 처리 */
        }

        Flow newFlow = new Flow(user, requestRegistFlowDto.getTitle(), parentFlow.getMusicPath());

        // 부모의 마지막 Record를 새로운 Flow의 Record 등록
        Record record = recordRepository.findLastRecordByFlowId(parentFlow.getId()).orElseThrow(/* TODO: 에러 처리 */);

        // Flow Closure 테이블에 추가


        // record 수정
    }

    public void release(Long userId, RequestReleaseFlowDto requestReleaseFlowDto) {
    }

    public FlowGraphDto graph(Long flowId) {
        // 해당 Flow가 속한 트리의 모든 노드들 가져오기
        FlowClosure flowClosure = flowClosureRepository.findFlowClosureByChildFlowId(flowId).orElseThrow(/* TODO: 에러처리 */);
        List<FlowClosure> flowClosures = flowClosureRepository.findFlowClosuresByRootFlow(flowClosure.getRootFlow());

        FlowGraphUtil flowGraphUtil = new FlowGraphUtil();
        return flowGraphUtil.makeGraph(flowClosures);
    }
}
