package com.ssafy.mugit.mugitory.dto.response;

import com.ssafy.mugit.mugitory.entity.embedded.UserDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.ssafy.mugit.fixure.FlowFixture.FLOW;
import static com.ssafy.mugit.fixure.MugitoryFixture.MUGITORY_TODAY;
import static com.ssafy.mugit.fixure.RecordFixture.RECORD;
import static com.ssafy.mugit.fixure.UserFixture.USER_WITH_PK;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("mugitory")
class ResponseMugitoryDtoTest {
    @Test
    @DisplayName("mugitoryDto 생성")
    void testCreateMugitoryDto() {
        // given
        Integer count = 3;
        UserDate userDate = new UserDate(1);

        // when
        ResponseMugitoryDto responseMugitoryDto = new ResponseMugitoryDto(MUGITORY_TODAY.getFixture(RECORD.getFixture(FLOW.getFixture(USER_WITH_PK.getFixture()))));

        // then
        assertThat(responseMugitoryDto).isNotNull();
        assertThat(responseMugitoryDto.getCount()).isEqualTo(1);
        assertThat(responseMugitoryDto.getDate()).isEqualTo(LocalDate.now().toString());
    }
}