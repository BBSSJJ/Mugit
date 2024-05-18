package com.ssafy.mugit.fixure;

import com.ssafy.mugit.record.dto.NewSourceDto;
import com.ssafy.mugit.record.dto.PreSourceDto;
import com.ssafy.mugit.record.dto.RecordRequestDto;
import com.ssafy.mugit.record.entity.Source;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public enum RecordRequestFixture {

    RECORD_REQUEST_FIXTURE("레코드 생성요청"),
    RECORD_REQUEST_FIXTURE_2("레코드 생성요청 2");

    private final String message;

    public RecordRequestDto getFixture(List<Source> sources) {
        List<NewSourceDto> newSources = List.of(
                new NewSourceDto("new source 1", "https://mugit.site/files/new_source_1"),
                new NewSourceDto("new source 2", "https://mugit.site/files/new_source_2"));
        List<PreSourceDto> preSources = sources.stream().map(s -> new PreSourceDto(s.getId(), "pre source name")).toList();
        return new RecordRequestDto(message, preSources, newSources);
    }
}
