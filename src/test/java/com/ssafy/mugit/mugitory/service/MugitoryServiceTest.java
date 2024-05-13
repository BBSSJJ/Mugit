package com.ssafy.mugit.mugitory.service;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.global.exception.MugitoryException;
import com.ssafy.mugit.mugitory.dto.response.ResponseMugitoryDto;
import com.ssafy.mugit.mugitory.dto.response.ResponseMugitoryRecordDto;
import com.ssafy.mugit.mugitory.entity.Mugitory;
import com.ssafy.mugit.mugitory.entity.embedded.UserDate;
import com.ssafy.mugit.mugitory.repository.MugitoryRepository;
import com.ssafy.mugit.record.dto.RecordRequestDto;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.entity.RecordSource;
import com.ssafy.mugit.record.entity.Source;
import com.ssafy.mugit.record.repository.RecordRepository;
import com.ssafy.mugit.record.repository.RecordSourceRepository;
import com.ssafy.mugit.record.repository.SourceRepository;
import com.ssafy.mugit.record.service.RecordDeleteService;
import com.ssafy.mugit.record.service.RecordInsertService;
import com.ssafy.mugit.record.util.ValidateUtil;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.ssafy.mugit.fixure.FlowFixture.FLOW;
import static com.ssafy.mugit.fixure.FlowFixture.FLOW_2;
import static com.ssafy.mugit.fixure.MugitoryFixture.*;
import static com.ssafy.mugit.fixure.RecordFixture.RECORD;
import static com.ssafy.mugit.fixure.RecordFixture.RECORD_2;
import static com.ssafy.mugit.fixure.RecordRequestFixture.RECORD_REQUEST_FIXTURE;
import static com.ssafy.mugit.fixure.RecordRequestFixture.RECORD_REQUEST_FIXTURE_2;
import static com.ssafy.mugit.fixure.SourceFixture.SOURCE;
import static com.ssafy.mugit.fixure.SourceFixture.SOURCE_2;
import static com.ssafy.mugit.fixure.UserFixture.USER;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("mugitory")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MugitoryServiceTest {

    @Autowired
    MugitoryRepository mugitoryRepository;

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    FlowRepository flowRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RecordSourceRepository recordSourceRepository;

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    EntityManager entityManager;

    MugitoryService sut;

    RecordInsertService recordInsertService;
    RecordDeleteService recordDeleteService;

    User user;
    Flow flow;
    Flow flow2;
    RecordSource recordSource;
    Source source;
    Source source2;
    Record record;
    Record record2;
    Mugitory mugitory;

    @BeforeEach
    void setUp() {
        System.out.println("---------save entity--------");
        sut = new MugitoryService(mugitoryRepository);
        recordInsertService = new RecordInsertService(sourceRepository, recordRepository, flowRepository, recordSourceRepository, sut, new ValidateUtil(flowRepository, recordRepository));
        recordDeleteService = new RecordDeleteService(recordRepository, recordSourceRepository, sut, new ValidateUtil(flowRepository, recordRepository));

        user = USER.getFixture();
        userRepository.save(user);

        flow = FLOW.getFixture(user);
        flow2 = FLOW_2.getFixture(user);
        flowRepository.saveAll(List.of(flow, flow2));

        record = RECORD.getFixture(flow);
        record2 = RECORD_2.getFixture(flow2);
        recordRepository.saveAll(List.of(record, record2));

        source = SOURCE.getFixture();
        source2 = SOURCE_2.getFixture();
        sourceRepository.saveAll(List.of(source, source2));

        recordSource = new RecordSource(record, source, "파일명");
        recordSourceRepository.save(recordSource);
        System.out.println("---------save entity--------");
    }

    @Test
    @Transactional
    @DisplayName("[통합] 뮤지토리 전체 조회(1년)")
    void test1YearMugitory() throws MugitoryException {
        // given
        mugitoryRepository.save(MUGITORY_TODAY.getFixture(record2));
        mugitoryRepository.save(MUGITORY_YESTERDAY.getFixture(record2));
        mugitoryRepository.save(MUGITORY_ONE_YEAR_BEFORE.getFixture(record2));
        mugitoryRepository.save(MUGITORY_ONE_YEAR_PLUS_ONE_DAY.getFixture(record2));

        // when
        List<ResponseMugitoryDto> responseMugitoryDtoList = sut.getOneYearMugitoryByUserId(user.getId());

        // then
        assertThat(responseMugitoryDtoList.size()).isEqualTo(3);
        assertThat(responseMugitoryDtoList).anyMatch(responseMugitoryDto -> responseMugitoryDto.getDate().equals(LocalDate.now().toString()))
                .anyMatch(responseMugitoryDto -> responseMugitoryDto.getDate().equals(LocalDate.now().minusDays(1).toString()))
                .anyMatch(responseMugitoryDto -> responseMugitoryDto.getDate().equals(LocalDate.now().minusYears(1).plusDays(1).toString()))
                .noneMatch(responseMugitoryDto -> responseMugitoryDto.getDate().equals(LocalDate.now().minusYears(1).toString()));
    }
    
    @Test
    @Transactional
    @DisplayName("[통합] 레코드 생성 시 뮤지토리 생성 / 레코드 삭제 시 뮤지토리 삭제")
    void testOneMugitory() {
        // given : 레코드 2개 생성
        RecordRequestDto recordRequestDto = RECORD_REQUEST_FIXTURE.getFixture(List.of(source, source2));
        RecordRequestDto recordRequestDto2 = RECORD_REQUEST_FIXTURE_2.getFixture(List.of(source, source2));

        // when : 오늘 날짜로 뮤지토리 조회
        recordInsertService.insertRecord(user.getId(), flow.getId(), recordRequestDto);
        recordInsertService.insertRecord(user.getId(), flow.getId(), recordRequestDto2);
        List<ResponseMugitoryRecordDto> recordDtos = sut.getMugitoryRecordByDate(user.getId(), LocalDate.now().toString());
        
        // then : 레코드 2개 조회
        assertThat(recordDtos.size()).isEqualTo(2);
        assertThat(recordDtos)
                .anySatisfy(recordDto -> assertThat(recordDto.getMessage()).isEqualTo("레코드 생성요청"))
                .anySatisfy(recordDto -> assertThat(recordDto.getMessage()).isEqualTo("레코드 생성요청 2"));

        // when2 : 1번 레코드 삭제
        Long record1Id = recordDtos.stream().filter(recordDto -> recordDto.getMessage().equals("레코드 생성요청")).findAny().get().getId();
        recordDeleteService.deleteRecord(user.getId(), record1Id);
        Mugitory mugitoryInDB = mugitoryRepository.findById(new UserDate(user.getId(), LocalDate.now())).get();
        entityManager.flush();

        // then2 : 2번 레코드만 남아있음
        assertThat(mugitoryInDB.getCount()).isEqualTo(1);
    }
}