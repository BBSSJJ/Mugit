package com.ssafy.mugit.mugitory.fixure;

import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.entity.RecordSource;
import com.ssafy.mugit.record.entity.Source;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RecordSourceFixture {
    RECORD_SOURCE;

    public RecordSource getFixture(Record record, Source source) {
        return new RecordSource(record, source, "file명");
    }
}
