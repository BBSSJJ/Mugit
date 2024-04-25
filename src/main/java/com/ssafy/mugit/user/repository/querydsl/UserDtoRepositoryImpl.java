package com.ssafy.mugit.user.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mugit.user.dto.response.QResponseUserProfileDto;
import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static com.ssafy.mugit.user.entity.QProfile.profile;
import static com.ssafy.mugit.user.entity.QUser.user;

@Repository
public class UserDtoRepositoryImpl implements UserDtoRepository {
    private final JPAQueryFactory queryFactory;

    public UserDtoRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public ResponseUserProfileDto findUserProfileDtoByNickName(String nickName) {
        return queryFactory.select(new QResponseUserProfileDto(user, profile))
                .from(profile)
                .leftJoin(profile.user, user).fetchJoin()
                .where(profile.nickName.eq(nickName))
                .fetchOne();
    }
}
