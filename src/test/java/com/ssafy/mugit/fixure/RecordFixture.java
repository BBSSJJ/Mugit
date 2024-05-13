package com.ssafy.mugit.fixure;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.entity.RecordSource;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum RecordFixture {
    RECORD(null,"레코드 생성", false),
    RECORD_2(null, "레코드2 생성", false);

    private final Long id;
    private final String message;
    private final Boolean isOpen;
    private final List<RecordSource> recordSources = new ArrayList<>();

    public Record getFixture(Flow flow) {
        return new Record(id, flow, message, isOpen, recordSources);
    }
}
