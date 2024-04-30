package com.ssafy.mugit.flow.main.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mugit.flow.main.entity.Flow;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static com.ssafy.mugit.flow.main.entity.QFlow.flow;
import static com.ssafy.mugit.user.entity.QProfile.profile;
import static com.ssafy.mugit.user.entity.QUser.user;

@Repository
public class CustomFlowRepositoryImpl implements CustomFlowRepository {

    private final JPAQueryFactory queryFactory;

    public CustomFlowRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Flow findByIdWithUser(Long flowId) {
        return queryFactory.selectFrom(flow)
                .leftJoin(flow.user, user).fetchJoin()
                .leftJoin(user.profile, profile).fetchJoin()
                .where(flow.id.eq(flowId))
                .fetchOne();
    }
}
