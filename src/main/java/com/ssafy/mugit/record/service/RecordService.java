package com.ssafy.mugit.record.service;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.record.dto.FilePathDto;
import com.ssafy.mugit.record.dto.RecordRequestDto;
import com.ssafy.mugit.record.dto.RecordResponseDto;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.entity.RecordSource;
import com.ssafy.mugit.record.entity.Source;
import com.ssafy.mugit.record.repository.RecordRepository;
import com.ssafy.mugit.record.repository.RecordSourceRepository;
import com.ssafy.mugit.record.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecordService {

    private final SourceRepository sourceRepository;
    private final RecordRepository recordRepository;
    private final FlowRepository flowRepository;
    private final RecordSourceRepository recordSourceRepository;

    public void insertRecord(Long flowId, RecordRequestDto recordRequestDto) {

        // 1. 레코드 생성
        Record record = createRecord(flowId, recordRequestDto.getMessage());

        // 2. 소스 파일 아이디와 레코드 아이디 매핑
        mappingRecordSource(record, recordRequestDto.getSourceIds());

        // 3. 추가된 소스 생성 및 매핑
        mappingRecordSource(record, createSource(recordRequestDto.getFilePaths()));

    }

    private Record createRecord(Long flowId, String message) {
        Flow flow = flowRepository.getReferenceById(flowId);
        Record record = Record.builder()
                .flow(flow)
                .message(message)
                .build();
        return recordRepository.save(record);
    }

    private void mappingRecordSource(Record record, List<Long> sourceIds) {
        if (sourceIds != null && !sourceIds.isEmpty()) {
            for (Long sourceId : sourceIds) {
                Source source = sourceRepository.getReferenceById(sourceId);
                RecordSource recordSource = RecordSource.builder()
                        .record(record)
                        .source(source)
                        .build();
                recordSourceRepository.save(recordSource);
            }
        }
    }

    private List<Long> createSource(List<FilePathDto> filePaths) {
        List<Long> ids = null;
        if (filePaths != null && !filePaths.isEmpty()) {
            ids = new ArrayList<>();
            for (FilePathDto filePath : filePaths) {
                Source source = Source.builder()
                        .uuidName(filePath.getUuidName())
                        .originName(filePath.getOriginName())
                        .build();
                ids.add(sourceRepository.save(source).getId());
            }
        }
        return ids;
    }

    public RecordResponseDto selectRecord(Long userId, Long recordId) {
        Record record = validateRecord(userId, recordId);
        List<Source> sources = new ArrayList<>();
        for (RecordSource rs : record.getRecordSources()) {
            sources.add(rs.getSource());
        }
        return new RecordResponseDto(record.getId(), record.getMessage(), sources);
    }

    public void deleteRecord(Long userId, Long recordId) {
        Record record = validateRecord(userId, recordId);
        recordRepository.delete(record);
    }

    public void validateFlow(Long userId, Long flowId) {
        Flow flow = flowRepository.findByIdWithUser(flowId);
        if (flow == null || !userId.equals(flow.getUser().getId())) {
            throw new UserApiException(UserApiError.NOT_ALLOWED_ACCESS);
        }
    }

    public Record validateRecord(Long userId, Long recordId) {
        Record record = recordRepository.findByIdWithUser(recordId);
        if(record == null || !userId.equals(record.getFlow().getUser().getId())) {
            throw new UserApiException(UserApiError.NOT_ALLOWED_ACCESS);
        }
        return record;
    }
}
