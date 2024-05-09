package com.ssafy.mugit.record.service;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.record.dto.NewSourceDto;
import com.ssafy.mugit.record.dto.PreSourceDto;
import com.ssafy.mugit.record.dto.RecordRequestDto;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.entity.RecordSource;
import com.ssafy.mugit.record.entity.Source;
import com.ssafy.mugit.record.repository.RecordRepository;
import com.ssafy.mugit.record.repository.RecordSourceRepository;
import com.ssafy.mugit.record.repository.SourceRepository;
import com.ssafy.mugit.record.util.ValidateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecordInsertService {

    private final SourceRepository sourceRepository;
    private final RecordRepository recordRepository;
    private final FlowRepository flowRepository;
    private final RecordSourceRepository recordSourceRepository;
    private final ValidateUtil validateUtil;

    public void insertRecord(Long userId, Long flowId, RecordRequestDto recordRequestDto) {

        // 1. 요청 검증
        validateUtil.validateFlow(userId, flowId);

        // 2. 레코드 생성
        Record record = createRecord(flowId, recordRequestDto.getMessage());

        // 3. 소스 파일 아이디와 레코드 아이디 매핑
        mappingRecordSource(record, recordRequestDto.getPreSources());

        // 4. 추가된 소스 생성 및 매핑
        mappingRecordSource(record, createSource(recordRequestDto.getNewSources()));
    }

    private Record createRecord(Long flowId, String message) {
        Flow flow = flowRepository.getReferenceById(flowId);
        Record record = Record.builder()
                .flow(flow)
                .message(message)
                .build();
        return recordRepository.save(record);
    }

    private void mappingRecordSource(Record record, List<PreSourceDto> preSources) {
        if (preSources != null && !preSources.isEmpty()) {
            for (PreSourceDto preSource : preSources) {
                Source source = sourceRepository.getReferenceById(preSource.getId());
                RecordSource recordSource = RecordSource.builder()
                        .record(record)
                        .source(source)
                        .name(preSource.getName())
                        .build();
                recordSourceRepository.save(recordSource);
            }
        }
    }

    private List<PreSourceDto> createSource(List<NewSourceDto> newSources) {
        List<PreSourceDto> sources = null;
        if (newSources != null && !newSources.isEmpty()) {
            sources = new ArrayList<>();
            for (NewSourceDto newSource : newSources) {
                Source source = Source.builder()
                        .path(newSource.getPath())
                        .build();
                sources.add(new PreSourceDto(sourceRepository.save(source).getId(), newSource.getName()));
            }
        }
        return sources;
    }
}
