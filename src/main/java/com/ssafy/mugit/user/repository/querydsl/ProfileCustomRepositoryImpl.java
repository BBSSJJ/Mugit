package com.ssafy.mugit.user.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mugit.user.entity.Profile;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static com.ssafy.mugit.user.entity.QProfile.profile;

@Repository
public class ProfileCustomRepositoryImpl implements ProfileCustomRepository {
    private final JPAQueryFactory queryFactory;
    public ProfileCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Profile findByUserId(Long userId) {
        return queryFactory.select(profile)
                .from(profile)
                .where(profile.user.id.eq(userId))
                .fetchOne();
    }
}
