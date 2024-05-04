package com.ssafy.mugit.flow.main.service;

import com.ssafy.mugit.flow.main.dto.FilePathDto;
import com.ssafy.mugit.flow.main.dto.FlowGraphDto;
import com.ssafy.mugit.flow.main.dto.request.RequestCreateNoteDto;
import com.ssafy.mugit.flow.main.dto.request.RequestReleaseFlowDto;
import com.ssafy.mugit.flow.main.entity.Authority;
import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.entity.FlowClosure;
import com.ssafy.mugit.flow.main.repository.FlowClosureRepository;
import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.hashtag.entity.Hashtag;
import com.ssafy.mugit.hashtag.service.HashtagService;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.entity.RecordSource;
import com.ssafy.mugit.record.entity.Source;
import com.ssafy.mugit.record.repository.RecordRepository;
import com.ssafy.mugit.record.repository.RecordSourceRepository;
import com.ssafy.mugit.record.repository.SourceRepository;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlowService {
    private final FlowRepository flowRepository;
    private final FlowClosureRepository flowClosureRepository;
    private final RecordRepository recordRepository;
    private final RecordSourceRepository recordSourceRepository;
    private final SourceRepository sourceRepository;
    private final UserRepository userRepository;
    private final HashtagService hashtagService;
    private final FlowHashtagService flowHashtagService;


    public void create(Long userId, RequestCreateNoteDto requestCreateNoteDto) {
        User user = userRepository.getReferenceById(userId);
        List<FilePathDto> files = requestCreateNoteDto.getFiles();
        String musicPath = null;
        String coverPath = null;
        for (FilePathDto file : files) {
            if (file.getType().equals("source")) {
                musicPath = file.getPath();
            }
            if (file.getType().equals("image")) {
                coverPath = file.getPath();
            }
        }

        // Flow 생성
        Flow note = new Flow(user,
                requestCreateNoteDto.getTitle(),
                requestCreateNoteDto.getMessage(),
                requestCreateNoteDto.getAuthority(),
                musicPath,
                coverPath);
        flowRepository.save(note);

        // 필요한 Hashtag 목록 추가
        List<Hashtag> hashtags = hashtagService.update(requestCreateNoteDto.getHashtags());

        // Flow Hashtag 연결테이블 생성
        flowHashtagService.addHashtags(note, hashtags);

        // Flow Closure 테이블에 추가
//        flowClosureRepository.save(new FlowClosure(note, note, note, 1));

        // Record 생성
        Source source = new Source(musicPath);
        sourceRepository.save(source);
        Record record = new Record(note, null, true);
        recordRepository.save(record);
        RecordSource recordSource = new RecordSource(record, source, null);
        recordSourceRepository.save(recordSource);
        record.getRecordSources().add(recordSource);
    }

    public void regist(Long userId, Long parentId) {

        User user = userRepository.getReferenceById(userId);
        Flow parentFlow = flowRepository.getReferenceById(parentId);

        // 권한 검사
        if ((parentFlow.getAuthority() == Authority.PROTECTED && !parentFlow.getId().equals(userId)) ||
                parentFlow.getAuthority() == Authority.PRIVATE) {
            /* TODO : 에러 처리 */
        }

        Flow newFlow = new Flow(user, "New Flow", parentFlow.getMusicPath());
        flowRepository.save(newFlow);
        Record newRecord = new Record(newFlow, null, true);
        recordRepository.save(newRecord);

        // 부모의 마지막 Record를 새로운 Flow의 Record 등록
        Record record = recordRepository.findLastRecordByFlowId(parentFlow.getId()).orElseThrow(/* TODO : 에러 처리 */);
        List<RecordSource> recordSources = record.getRecordSources();
        List<RecordSource> newRecordSources = new ArrayList<>();
        recordSources.forEach((recordSource) -> {
            newRecordSources.add(new RecordSource(newRecord, recordSource.getSource(), recordSource.getName()));
        });
        recordSourceRepository.saveAll(newRecordSources);
        newRecord.initRecordSources(newRecordSources);

        // Flow Closure 테이블에 추가

    }

    public void release(Long userId, Long flowId, RequestReleaseFlowDto requestReleaseFlowDto) {
        User user = userRepository.getReferenceById(userId);
        Flow flow = flowRepository.getReferenceById(flowId);
        List<FilePathDto> files = requestReleaseFlowDto.getFiles();
        String musicPath = null;
        String coverPath = null;
        for (FilePathDto file : files) {
            if (file.getType().equals("source")) {
                musicPath = file.getPath();
            }
            if (file.getType().equals("image")) {
                coverPath = file.getPath();
            }
        }

        if (!flow.getUser().equals(user)) {
            /* TODO : 에러 처리*/
        }
        if (flow.isReleased()) {
            /* TODO : 에러 처리*/
        }

        // Flow 릴리즈
        flow.releaseFlow(requestReleaseFlowDto.getTitle(),
                requestReleaseFlowDto.getMessage(),
                requestReleaseFlowDto.getAuthority(),
                musicPath,
                coverPath
        );

        // 필요한 Hashtag 목록 추가
        List<Hashtag> hashtags = hashtagService.update(requestReleaseFlowDto.getHashtags());

        // Flow Hashtag 연결테이블 생성
        flowHashtagService.addHashtags(flow, hashtags);
    }

    public FlowGraphDto graph(Long flowId) {
        // 해당 Flow가 속한 트리의 모든 노드들 가져오기
        FlowClosure flowClosure = flowClosureRepository.findFlowClosureByChildFlowId(flowId).orElseThrow(/* TODO: 에러처리 */);
        List<FlowClosure> flowClosures = flowClosureRepository.findFlowClosuresByRootFlow(flowClosure.getRootFlow());

        FlowGraphUtil flowGraphUtil = new FlowGraphUtil();
        return flowGraphUtil.makeGraph(flowClosures);
    }
}
