package com.ssafy.mugit.mugitory.acceptance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.global.annotation.AcceptanceTest;
import com.ssafy.mugit.global.dto.ListDto;
import com.ssafy.mugit.global.exception.MugitoryException;
import com.ssafy.mugit.mugitory.dto.response.ResponseMugitoryDto;
import com.ssafy.mugit.mugitory.entity.Mugitory;
import com.ssafy.mugit.fixure.MugitoryFixture;
import com.ssafy.mugit.mugitory.repository.MugitoryRepository;
import com.ssafy.mugit.mugitory.service.MugitoryService;
import com.ssafy.mugit.record.dto.RecordDto;
import com.ssafy.mugit.record.entity.Record;
import com.ssafy.mugit.record.entity.RecordSource;
import com.ssafy.mugit.record.entity.Source;
import com.ssafy.mugit.record.repository.RecordRepository;
import com.ssafy.mugit.record.repository.RecordSourceRepository;
import com.ssafy.mugit.record.repository.SourceRepository;
import com.ssafy.mugit.record.service.RecordInsertService;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static com.ssafy.mugit.fixure.FlowFixture.FLOW;
import static com.ssafy.mugit.fixure.FlowFixture.FLOW_2;
import static com.ssafy.mugit.fixure.RecordFixture.RECORD;
import static com.ssafy.mugit.fixure.RecordFixture.RECORD_2;
import static com.ssafy.mugit.fixure.RecordRequestFixture.RECORD_REQUEST_FIXTURE;
import static com.ssafy.mugit.fixure.RecordRequestFixture.RECORD_REQUEST_FIXTURE_2;
import static com.ssafy.mugit.fixure.SourceFixture.SOURCE;
import static com.ssafy.mugit.fixure.SourceFixture.SOURCE_2;
import static com.ssafy.mugit.fixure.ProfileFixture.PROFILE;
import static com.ssafy.mugit.fixure.UserFixture.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("mugitory")
@AcceptanceTest
public class MugitoryAcceptanceTest {
    @Autowired
    MockMvc mockMvc;

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
    RecordInsertService recordInsertService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EntityManager entityManager;

    User user;
    Flow flow;
    Flow flow2;
    RecordSource recordSource;
    Source source;
    Source source2;
    Record record;
    Record record2;
    Mugitory mugitory;

    @Autowired
    MugitoryService mugitoryService;

    @BeforeEach
    void setUp() throws MugitoryException {
        user = USER.getFixture(PROFILE.getFixture());
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
    }

    @Test
    @DisplayName("[인수] 뮤지토리 정상 조회시 200 + 1년 이내의 모든 뮤지토리 받아오기(200)")
    void testAllMugitoryInYear() throws Exception {
        // given
        mugitoryRepository.save(MugitoryFixture.MUGITORY_TODAY.getFixture(record2));
        mugitoryRepository.save(MugitoryFixture.MUGITORY_YESTERDAY.getFixture(record2));
        mugitoryRepository.save(MugitoryFixture.MUGITORY_ONE_YEAR_BEFORE.getFixture(record2));
        mugitoryRepository.save(MugitoryFixture.MUGITORY_ONE_YEAR_PLUS_ONE_DAY.getFixture(record2));
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")).andReturn().getResponse().getCookies();

        // when
        String content = mockMvc.perform(get("/api/mugitories").cookie(loginCookie))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        ListDto<List<ResponseMugitoryDto>> responseMugitoryDtoList = objectMapper.readValue(content, new TypeReference<ListDto<List<ResponseMugitoryDto>>>() {
        });
        List<ResponseMugitoryDto> responseMugitoryDtos = responseMugitoryDtoList.getList();

        // then
        assertThat(responseMugitoryDtos.size()).isEqualTo(3);
        assertThat(responseMugitoryDtos).anyMatch(responseMugitoryDto -> responseMugitoryDto.getDate().equals(LocalDate.now().toString()))
                .anyMatch(responseMugitoryDto -> responseMugitoryDto.getDate().equals(LocalDate.now().minusDays(1).toString()))
                .anyMatch(responseMugitoryDto -> responseMugitoryDto.getDate().equals(LocalDate.now().minusYears(1).plusDays(1).toString()))
                .noneMatch(responseMugitoryDto -> responseMugitoryDto.getDate().equals(LocalDate.now().minusYears(1).toString()));
    }

    @Test
    @DisplayName("[인수] 뮤지토리 단건 조회시 해당 뮤지토리가 가진 레코드 전체 조회(200)")
    void testGetMugitoryByDate() throws Exception {
        // given
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")).andReturn().getResponse().getCookies();

        // when 1
        ResultActions perform = mockMvc.perform(get("/api/mugitories/" + LocalDate.now().toString()).cookie(loginCookie));

        // then 1
        perform.andExpect(status().is(404));

        // when 2
        recordInsertService.insertRecord(user.getId(), flow.getId(), RECORD_REQUEST_FIXTURE.getFixture(List.of(source, source2)));
        recordInsertService.insertRecord(user.getId(), flow.getId(), RECORD_REQUEST_FIXTURE_2.getFixture(List.of(source, source2)));
        String content = mockMvc.perform(get("/api/mugitories/" + LocalDate.now().toString()).cookie(loginCookie))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        ListDto<List<RecordDto>> recordDtoList = objectMapper.readValue(content, new TypeReference<ListDto<List<RecordDto>>>() {
        });

        // then 2
        assertThat(recordDtoList.getList().size()).isEqualTo(2);
        assertThat(recordDtoList.getList())
                .anySatisfy(recordDto -> assertThat(recordDto.getMessage()).isEqualTo("레코드 생성요청"))
                .anySatisfy(recordDto -> assertThat(recordDto.getMessage()).isEqualTo("레코드 생성요청 2"));
    }

    @Test
    @DisplayName("[인수] 레코드 생성 시 뮤지토리 정상 조회, 레코드 삭제 시 뮤지토리 정상 삭제")
    void testCreateAndDeleteRecordEffectsMugitory() throws Exception {
        // given
        Cookie[] loginCookie = mockMvc.perform(get("/api/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")).andReturn().getResponse().getCookies();
        String body = objectMapper.writeValueAsString(RECORD_REQUEST_FIXTURE.getFixture(List.of(source, source2)));
        String body2 = objectMapper.writeValueAsString(RECORD_REQUEST_FIXTURE_2.getFixture(List.of(source, source2)));

        // when 1 : record 2개 생성
        mockMvc.perform(post("/api/records/flows/" + flow.getId()).cookie(loginCookie).contentType(MediaType.APPLICATION_JSON).content(body));
        mockMvc.perform(post("/api/records/flows/" + flow2.getId()).cookie(loginCookie).contentType(MediaType.APPLICATION_JSON).content(body2));
        String content = mockMvc.perform(get("/api/mugitories/" + LocalDate.now().toString()).cookie(loginCookie))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        ListDto<List<RecordDto>> recordDtoList = objectMapper.readValue(content, new TypeReference<ListDto<List<RecordDto>>>() {
        });

        // then 1 : 뮤지토리 조회 시 200, 레코드 2개 확인
        assertThat(recordDtoList.getList().size()).isEqualTo(2);
        assertThat(recordDtoList.getList())
                .anySatisfy(recordDto -> assertThat(recordDto.getMessage()).isEqualTo("레코드 생성요청"))
                .anySatisfy(recordDto -> assertThat(recordDto.getMessage()).isEqualTo("레코드 생성요청 2"));

        // when 2 : record 2개 삭제 후 뮤지토리 조회
        RecordDto recordDto1 = recordDtoList.getList().get(0);
        RecordDto recordDto2 = recordDtoList.getList().get(1);
        mockMvc.perform(delete("/api/records/" + recordDto1.getId()).cookie(loginCookie)).andExpect(status().is(200));
        mockMvc.perform(delete("/api/records/" + recordDto2.getId()).cookie(loginCookie)).andExpect(status().is(200));

        // then 2 : 뮤지토리 조회 시 404
        mockMvc.perform(get("/api/mugitories/" + LocalDate.now().toString()).cookie(loginCookie))
                .andExpect(status().is(404));
    }
}
