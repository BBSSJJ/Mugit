package com.ssafy.mugit.record.service;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.record.dto.RecordResponseDto;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.entity.RecordSource;
import com.ssafy.mugit.record.entity.Source;
import com.ssafy.mugit.record.repository.RecordRepository;
import com.ssafy.mugit.record.repository.RecordSourceRepository;
import com.ssafy.mugit.record.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecordService {

    private final SourceRepository sourceRepository;
    private final RecordRepository recordRepository;
    private final FlowRepository flowRepository;
    private final RecordSourceRepository recordSourceRepository;

    public void insertRecord(Long flowId, String message, List<Long> sourceIds, Map<String, String> filePaths) {

        // 1. 레코드 생성
        Record record = createRecord(flowId, message);

        // 2. 소스 파일 아이디와 레코드 아이디 매핑
        mappingRecordSource(record, sourceIds);

        // 3. 추가된 소스 생성 및 매핑
        mappingRecordSource(record, createSource(filePaths));

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
        if(sourceIds != null && !sourceIds.isEmpty()){
            for(Long sourceId : sourceIds) {
                Source source = sourceRepository.getReferenceById(sourceId);
                RecordSource recordSource = RecordSource.builder()
                        .record(record)
                        .source(source)
                        .build();
                recordSourceRepository.save(recordSource);
            }
        }
    }

    private List<Long> createSource(Map<String, String> filePaths) {
        List<Long> ids = null;
        if(filePaths != null && !filePaths.isEmpty()) {
            ids = new ArrayList<>();
            for(String key : filePaths.keySet()) {
                Source source = Source.builder()
                        .uuidName(key)
                        .originName(filePaths.get(key))
                        .build();
                ids.add(sourceRepository.save(source).getId());
            }
        }
        return ids;
    }

    public RecordResponseDto selectRecord(Long recordId) {
        Record record = recordRepository.findByIdWithSources(recordId);
        List<Source> sources = new ArrayList<>();
        for(RecordSource rs : record.getRecordSources()) {
            sources.add(rs.getSource());
        }
        return new RecordResponseDto(record.getId(), record.getMessage(), sources);
    }

    public String deleteRecord(Long recordId) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record is not existed."));
        recordRepository.delete(record);
        return "record delete successful";
    }
}
