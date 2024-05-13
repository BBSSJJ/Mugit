package com.ssafy.mugit.flow.main.service;

import com.ssafy.mugit.flow.main.dto.FilePathDto;
import com.ssafy.mugit.flow.main.dto.request.RequestCreateNoteDto;
import com.ssafy.mugit.flow.main.dto.request.RequestReleaseFlowDto;
import com.ssafy.mugit.flow.main.entity.Authority;
import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.global.exception.FlowApiException;
import com.ssafy.mugit.global.exception.error.FlowApiError;
import com.ssafy.mugit.hashtag.entity.Hashtag;
import com.ssafy.mugit.hashtag.service.HashtagService;
import com.ssafy.mugit.notification.service.NotificationService;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.entity.RecordSource;
import com.ssafy.mugit.record.entity.Source;
import com.ssafy.mugit.record.repository.RecordRepository;
import com.ssafy.mugit.record.repository.RecordSourceRepository;
import com.ssafy.mugit.record.repository.SourceRepository;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlowService {
    private final FlowRepository flowRepository;
    private final RecordRepository recordRepository;
    private final RecordSourceRepository recordSourceRepository;
    private final SourceRepository sourceRepository;
    private final UserRepository userRepository;
    private final HashtagService hashtagService;
    private final FlowHashtagService flowHashtagService;
    private final NotificationService notificationService;


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
        if (musicPath == null) {
            throw new FlowApiException(FlowApiError.NO_MUSIC);
        }

        // Flow 생성
        Flow note = new Flow(user,
                requestCreateNoteDto.getTitle(),
                requestCreateNoteDto.getMessage(),
                requestCreateNoteDto.getAuthority(),
                musicPath,
                coverPath);
        flowRepository.save(note);

        if (requestCreateNoteDto.getHashtags() != null && !requestCreateNoteDto.getHashtags().isEmpty()) {
            // 필요한 Hashtag 목록 추가
            List<Hashtag> hashtags = hashtagService.update(requestCreateNoteDto.getHashtags());

            // Flow Hashtag 연결테이블 생성
            flowHashtagService.addHashtags(note, hashtags);
        }

        // Flow의 부모, 루트 추가
        note.initParentAndRoot(note, null);

        // Record 생성
        Source source = new Source(musicPath);
        sourceRepository.save(source);
        Record record = new Record(note, null, true);
        recordRepository.save(record);
        RecordSource recordSource = new RecordSource(record, source, null);
        recordSourceRepository.save(recordSource);
        record.getRecordSources().add(recordSource);
    }

    @Transactional
    public void regist(Long userId, Long parentId) {

        User user = userRepository.getReferenceById(userId);
        Flow parentFlow = flowRepository.getReferenceById(parentId);

        // 권한 검사
        if (!parentFlow.isReleased()) {
            throw new FlowApiException(FlowApiError.NOT_RELEASED_FLOW);
        }

        if ((parentFlow.getAuthority() == Authority.PROTECTED && !parentFlow.getId().equals(userId)) ||
                parentFlow.getAuthority() == Authority.PRIVATE) {
            throw new FlowApiException(FlowApiError.NOT_ALLOWED_ACCESS);
        }

        Flow newFlow = new Flow(user, "New Flow", parentFlow.getMusicPath());
        flowRepository.save(newFlow);
        Record newRecord = new Record(newFlow, null, true);
        recordRepository.save(newRecord);

        // 부모의 마지막 Record를 새로운 Flow의 Record 등록
        Record record = recordRepository.findLastRecordByFlowId(parentFlow.getId()).orElseThrow(() -> new FlowApiException(FlowApiError.NO_RECORD));
        List<RecordSource> recordSources = record.getRecordSources();
        List<RecordSource> newRecordSources = newRecord.getRecordSources();
        recordSources.forEach((recordSource) -> {
            newRecordSources.add(new RecordSource(newRecord, recordSource.getSource(), recordSource.getName()));
        });
        newRecord.initRecordSources(newRecordSources);

        // Flow의 부모, 루트 추가
        newFlow.initParentAndRoot(parentFlow.getRootFlow(), parentFlow);
    }

    @Transactional
    public void release(Long userId, Long flowId, RequestReleaseFlowDto requestReleaseFlowDto) {
        User user = userRepository.getReferenceById(userId);
        Flow flow = flowRepository.findFlowAndParentByFlowId(flowId).orElseThrow(() -> new FlowApiException(FlowApiError.NOT_EXIST_FLOW));
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
        if (musicPath == null) {
            throw new FlowApiException(FlowApiError.NO_MUSIC);
        }
        if (!flow.getUser().equals(user)) {
            throw new FlowApiException(FlowApiError.NOT_ALLOWED_ACCESS);
        }
        if (flow.isReleased()) {
            throw new FlowApiException(FlowApiError.ALREADY_RELEASED_FLOW);
        }

        // Flow 릴리즈
        flow.releaseFlow(requestReleaseFlowDto.getTitle(),
                requestReleaseFlowDto.getMessage(),
                requestReleaseFlowDto.getAuthority(),
                musicPath,
                coverPath
        );

        if (requestReleaseFlowDto.getHashtags() != null && !requestReleaseFlowDto.getHashtags().isEmpty()) {
            // 필요한 Hashtag 목록 추가
            List<Hashtag> hashtags = hashtagService.update(requestReleaseFlowDto.getHashtags());

            // Flow Hashtag 연결테이블 생성
            flowHashtagService.addHashtags(flow, hashtags);
        }

        // 알림 생성
        if (!flow.getAuthority().equals(Authority.PRIVATE)) {
            notificationService.sendFlowRelease(flow.getUser(), flow.getParentFlow().getUser(), flow);
        }
    }
}
