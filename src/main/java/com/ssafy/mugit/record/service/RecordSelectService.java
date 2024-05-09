package com.ssafy.mugit.record.service;

import com.ssafy.mugit.record.dto.RecordResponseDto;
import com.ssafy.mugit.record.dto.SourceInfoDto;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.entity.RecordSource;
import com.ssafy.mugit.record.util.ValidateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordSelectService {

    private final ValidateUtil validateUtil;

    public RecordResponseDto selectRecord(Long userId, Long recordId) {
        Record record = validateUtil.validateRecord(userId, recordId);
        List<SourceInfoDto> sources = new ArrayList<>();
        for (RecordSource rs : record.getRecordSources()) {
            SourceInfoDto sourceInfoDto = SourceInfoDto.builder()
                    .id(rs.getSource().getId())
                    .name(rs.getName())
                    .path(rs.getSource().getPath())
                    .build();
            sources.add(sourceInfoDto);
        }
        return new RecordResponseDto(record.getId(), record.getMessage(), sources);
    }
}
