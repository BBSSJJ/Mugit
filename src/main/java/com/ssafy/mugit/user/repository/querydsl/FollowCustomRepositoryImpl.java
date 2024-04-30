package com.ssafy.mugit.user.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mugit.user.dto.FollowerDto;
import com.ssafy.mugit.user.dto.QFollowerDto;
import com.ssafy.mugit.user.entity.Follow;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.ssafy.mugit.user.entity.QFollow.follow;
import static com.ssafy.mugit.user.entity.QUser.user;

public class FollowCustomRepositoryImpl implements FollowCustomRepository {

    private final JPAQueryFactory queryFactory;

    public FollowCustomRepositoryImpl(@Autowired EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Long countMyFollowers(long myId) {
        return queryFactory
                .select(follow.count())
                .from(follow)
                .leftJoin(follow.follower)
                .where(follow.follower.id.eq(myId)).fetchOne();
    }

    @Override
    public Long countMyFollowings(long myId) {
        return queryFactory
                .select(follow.count())
                .from(follow)
                .leftJoin(follow.following)
                .where(follow.following.id.eq(myId)).fetchOne();
    }

    @Override
    public List<FollowerDto> findAllFollowers(long myId) {
        return queryFactory
                .select(new QFollowerDto(user))
                .from(follow)
                .leftJoin(follow.following)
                .leftJoin(follow.following.profile)
                .where(follow.follower.id.eq(myId)).fetch();
    }

    @Override
    public List<FollowerDto> findAllFollowings(long myId) {
        return queryFactory
                .select(new QFollowerDto(user))
                .from(follow)
                .leftJoin(follow.follower)
                .leftJoin(follow.follower.profile)
                .where(follow.following.id.eq(myId)).fetch();
    }


    @Override
    public boolean existsFollow(Long followerId, Long followingId) {
        Integer ret = queryFactory
                .selectOne()
                .from(follow)
                .where(follow.follower.id.eq(followerId)
                        .and(follow.following.id.eq(followingId)))
                .fetchFirst();
        return ret != null;
    }

    @Override
    public Follow findByFollowerIdAndFollowingId(Long followerId, Long followingId) {
        return queryFactory
                .selectFrom(follow)
                .where(follow.follower.id.eq(followerId)
                        .and(follow.following.id.eq(followingId))).fetchOne();
    }
}
