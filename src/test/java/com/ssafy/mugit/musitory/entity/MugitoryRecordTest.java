package com.ssafy.mugit.musitory.entity;

import com.ssafy.mugit.record.entity.Record;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.ssafy.mugit.musitory.fixure.MugitoryFixture.MUGITORY;
import static com.ssafy.mugit.musitory.fixure.RecordFixture.RECORD;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("mugitory")
class MugitoryRecordTest {

    @Test
    @DisplayName("뮤지토리 레코드 엔티티 생성")
    void testCreateMusitoryRecordEntity() {
        // given
        Record record = RECORD.getFixture();
        Mugitory mugitory = MUGITORY.getFixture(record);

        // when
        MugitoryRecord musitoryRecord = new MugitoryRecord(mugitory, record);

        // then
        assertThat(musitoryRecord).isNotNull();
        assertThat(musitoryRecord.getMugitory()).isEqualTo(mugitory);
        assertThat(musitoryRecord.getRecord()).isEqualTo(record);
    }
}