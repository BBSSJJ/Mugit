package com.ssafy.mugit.musitory.entity;

import com.ssafy.mugit.global.exception.MugitoryException;
import com.ssafy.mugit.global.exception.error.MugitoryError;
import com.ssafy.mugit.record.entity.Record;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.ssafy.mugit.musitory.fixure.RecordFixture.RECORD;
import static com.ssafy.mugit.musitory.fixure.RecordFixture.RECORD_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("mugitory")
class MugitoryEntityTest {
    @Test
    @DisplayName("뮤지토리 생성 시 사용자 pk와 레코드 pk를 받아서 저장")
    void testCreateMusitoryEntity() {
        // given
        long userId = 1;
        Record record = RECORD.getFixture();

        // when
        Mugitory mugitory = new Mugitory(userId, record);
        LocalDate today = LocalDate.now();

        // then
        assertThat(mugitory.getCount()).isEqualTo(1);
        assertThat(mugitory.getUserDate().getDate()).isEqualTo(today);
        assertThat(mugitory.getMugitoryRecord().get(0).getRecord()).isEqualTo(record);
    }

    @Test
    @DisplayName("동일 레코드 추가 시 오류")
    void testMugitoryAddSameRecord() {
        // given
        long userId = 1;
        Record record = RECORD.getFixture();
        Mugitory mugitory = new Mugitory(userId, record);

        // when : NOTHING

        // then
        assertThatThrownBy(() -> mugitory.addRecord(record))
                .isInstanceOf(MugitoryException.class)
                .hasMessage(MugitoryError.ALREADY_RECORDED_TO_MUGITORY.getMessage());
    }

    @Test
    @DisplayName("새로운 레코드 추가")
    void testMugitoryAddNewRecord() throws MugitoryException {
        // given
        long userId = 1;
        Record record = RECORD.getFixture();
        Record record2 = RECORD_2.getFixture();
        Mugitory mugitory = new Mugitory(userId, record);

        // when
        mugitory.addRecord(record2);

        // then
        assertThat(mugitory.getCount()).isEqualTo(2);
        assertThat(mugitory.getMugitoryRecord()).anyMatch(r -> r.getRecord().equals(record));
        assertThat(mugitory.getMugitoryRecord()).anyMatch(r -> r.getRecord().equals(record2));
    }

    @Test
    @DisplayName("레코드 1개 삭제 시 오류")
    void testDeleteMugitoryRecordWhichHasOneRecord() {
        // given
        long userId = 1;
        Record record = RECORD.getFixture();

        // when
        Mugitory mugitory = new Mugitory(userId, record);

        // then
        assertThatThrownBy(() -> mugitory.deleteRecord(record))
                .isInstanceOf(MugitoryException.class)
                .hasMessage(MugitoryError.DELETE_ALL_RECORD_IN_MUGITORY.getMessage());
    }

    @Test
    @DisplayName("없는 삭제 시 오류")
    void testDeleteMugitoryRecordWhichHasNotRecorded() {
        // given
        long userId = 1;
        Record record = RECORD.getFixture();
        Record record2 = RECORD_2.getFixture();

        // when
        Mugitory mugitory = new Mugitory(userId, record);

        // then
        assertThatThrownBy(() -> mugitory.deleteRecord(record2))
                .isInstanceOf(MugitoryException.class)
                .hasMessage(MugitoryError.DELETE_RECORD_NOT_IN_MUGITORY.getMessage());
    }

    @Test
    @DisplayName("레코드 삭제")
    void testDeleteMugitoryRecord() throws MugitoryException {
        // given
        long userId = 1;
        Record record = RECORD.getFixture();
        Record record2 = RECORD_2.getFixture();
        Mugitory mugitory = new Mugitory(userId, record);
        mugitory.addRecord(record2);

        // when
        mugitory.deleteRecord(record2);

        // then
        assertThat(mugitory.getCount()).isEqualTo(1);
        assertThat(mugitory.getMugitoryRecord()).anyMatch(r -> r.getRecord().equals(record));
        assertThat(mugitory.getMugitoryRecord()).noneMatch(r -> r.getRecord().equals(record2));
    }
}