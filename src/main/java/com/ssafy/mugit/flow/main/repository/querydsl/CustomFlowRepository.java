package com.ssafy.mugit.flow.main.repository.querydsl;

import com.ssafy.mugit.flow.main.entity.Flow;

public interface CustomFlowRepository {

    Flow findByIdWithUser(Long flowId);

}
