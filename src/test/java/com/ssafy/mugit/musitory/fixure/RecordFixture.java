package com.ssafy.mugit.musitory.fixure;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.entity.RecordSource;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static com.ssafy.mugit.musitory.fixure.FlowFixture.FLOW;

@Getter
@AllArgsConstructor
public enum RecordFixture {
    RECORD(1L, FLOW.getFixture(), "레코드 생성", false, null),
    RECORD_2(2L, FLOW.getFixture(), "레코드2 생성", false, null);

    private final Long id;
    private final Flow flow;
    private final String message;
    private final Boolean isOpen;
    private final List<RecordSource> recordSources;

    public Record getFixture() {
        return new Record(id, flow, message, isOpen, recordSources);
    }
}
