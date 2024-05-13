package com.ssafy.mugit.mugitory.entity;

import com.ssafy.mugit.global.exception.MugitoryException;
import com.ssafy.mugit.global.exception.error.MugitoryError;
import com.ssafy.mugit.record.entity.Record;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.ssafy.mugit.mugitory.fixure.FlowFixture.FLOW;
import static com.ssafy.mugit.mugitory.fixure.RecordFixture.RECORD;
import static com.ssafy.mugit.mugitory.fixure.RecordFixture.RECORD_2;
import static com.ssafy.mugit.user.fixture.UserFixture.USER_WITH_PK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("mugitory")
class MugitoryEntityTest {
    @Test
    @DisplayName("뮤지토리 생성 시 사용자 pk와 레코드 pk를 받아서 저장")
    void testCreateMusitoryEntity() throws MugitoryException {
        // given
        long userId = 1;
        Record record = RECORD.getFixture(FLOW.getFixture(USER_WITH_PK.getFixture()));

        // when
        Mugitory mugitory = new Mugitory(record);
        LocalDate today = LocalDate.now();

        // then
        assertThat(mugitory.getCount()).isEqualTo(1);
        assertThat(mugitory.getUserDateId().getDate()).isEqualTo(today);
        assertThat(mugitory.getRecords().get(0)).isEqualTo(record);
    }

    @Test
    @DisplayName("새로운 레코드 추가")
    void testMugitoryAddNewRecord() throws MugitoryException {
        // given
        long userId = 1;
        Record record = RECORD.getFixture(FLOW.getFixture(USER_WITH_PK.getFixture()));
        Record record2 = RECORD_2.getFixture(FLOW.getFixture(USER_WITH_PK.getFixture()));
        Mugitory mugitory = new Mugitory(record);

        // when
        mugitory.addRecord(record2);

        // then
        assertThat(mugitory.getCount()).isEqualTo(2);
        assertThat(mugitory.getRecords()).anyMatch(r -> r.equals(record));
        assertThat(mugitory.getRecords()).anyMatch(r -> r.equals(record2));
    }

    @Test
    @DisplayName("레코드 1개 삭제 시 오류")
    void testDeleteMugitoryRecordWhichHasOneRecord() {
        // given
        long userId = 1;
        Record record = RECORD.getFixture(FLOW.getFixture(USER_WITH_PK.getFixture()));

        // when
        Mugitory mugitory = new Mugitory(record);

        // then
        assertThatThrownBy(() -> mugitory.deleteRecord(record))
                .isInstanceOf(MugitoryException.class)
                .hasMessage(MugitoryError.DELETE_ALL_RECORD_IN_MUGITORY.getMessage());
    }

    @Test
    @DisplayName("레코드 삭제")
    void testDeleteMugitoryRecord() throws MugitoryException {
        // given
        long userId = 1;
        Record record = RECORD.getFixture(FLOW.getFixture(USER_WITH_PK.getFixture()));
        Record record2 = RECORD_2.getFixture(FLOW.getFixture(USER_WITH_PK.getFixture()));
        Mugitory mugitory = new Mugitory(record);
        mugitory.addRecord(record2);

        // when
        mugitory.deleteRecord(record2);

        // then
        assertThat(mugitory.getCount()).isEqualTo(1);
        assertThat(mugitory.getRecords()).anyMatch(r -> r.equals(record));
    }
}