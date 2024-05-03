package com.ssafy.mugit.flow.main.service;

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
        Flow note = new Flow(user,
                requestCreateNoteDto.getTitle(),
                requestCreateNoteDto.getMessage(),
                requestCreateNoteDto.getAuthority(),
                requestCreateNoteDto.getMusicPath(),
                requestCreateNoteDto.getCoverPath());
        flowRepository.save(note);

        System.out.println("in service : " + requestCreateNoteDto.getHashtags());

        List<Hashtag> hashtags = hashtagService.update(requestCreateNoteDto.getHashtags());

        flowHashtagService.addHashtags(note, hashtags);

        flowClosureRepository.save(new FlowClosure(note, note, note, 0));

        //record 생성!
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

        Record record = recordRepository.findLastRecordByFlowId(parentFlow.getId()).orElseThrow(/* TODO: 에러 처리 */);

        // record 수정
//        Record newRecord = new Record(newFlow, null, true, record.getRecordSources());
//        newFlow.getRecords().add(newRecord);

        //flow closure repository에서 해당 record의 모든 부모들을 부모로 만드는 로직 생성

    }

    public void release(Long userId, RequestReleaseFlowDto requestReleaseFlowDto) {
    }
}
