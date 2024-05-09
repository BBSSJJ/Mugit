package com.ssafy.mugit.musitory.service;

import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.global.exception.MugitoryException;
import com.ssafy.mugit.global.util.AcceptanceTestExecutionListener;
import com.ssafy.mugit.musitory.entity.Mugitory;
import com.ssafy.mugit.musitory.entity.MugitoryRecord;
import com.ssafy.mugit.musitory.repository.MugitoryRepository;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.repository.RecordRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.transaction.annotation.Transactional;

import static com.ssafy.mugit.musitory.fixure.MugitoryFixture.MUGITORY;
import static com.ssafy.mugit.musitory.fixure.RecordFixture.RECORD;
import static com.ssafy.mugit.musitory.fixure.RecordFixture.RECORD_2;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("mugitory")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class MugitoryServiceTest {

    @Autowired
    MugitoryRepository mugitoryRepository;

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    FlowRepository flowRepository;

    @Autowired
    UserRepository userRepository;

    MugitoryService sut;

    Record record = RECORD.getFixture();
    Mugitory mugitory = MUGITORY.getFixture(record);

    @BeforeEach
    void setUp() throws MugitoryException {
        sut = new MugitoryService(mugitoryRepository);
        userRepository.save(record.getFlow().getUser());
        flowRepository.save(record.getFlow());
        recordRepository.save(record);
        mugitoryRepository.save(mugitory);
        recordRepository.flush();
    }

    @Test
    @Transactional
    @DisplayName("[통합] 레코드 1 -> 0 이면 뮤지토리 삭제")
    void deleteRecord() {
        // given
        recordRepository.delete(record);

        // when
        sut.deleteRecord(mugitory, record);

        // then
        assertThat(mugitoryRepository.existsById(mugitory.getId())).isFalse();
    }
    
    @Test
    @Transactional
    @DisplayName("[통합] uk로 조회했는데 이미 뮤지토리가 존재하면 count +1 / record pk 저장")
    void testSaveMugitory() {
        // given
        Record record = RECORD_2.getFixture();

        // when
        sut.recordMugitory(record, mugitory);
        MugitoryRecord mugitoryRecord = mugitoryRepository.findByIdWithMugitoryRecord(this.mugitory.getId());

        // then
        assertThat(mugitory.getCount()).isEqualTo(2);
        assertThat(mugitory.getMugitoryRecord()).anyMatch(mr -> mr.getRecord().equals(record));
    }
}