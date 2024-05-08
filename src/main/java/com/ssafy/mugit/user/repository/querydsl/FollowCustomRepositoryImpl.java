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
    public Long countMyFollowers(long myId) {  // 나를 팔로잉하는 사람 숫자(나 : 팔로이)
        return queryFactory
                .select(follow.count())
                .from(follow)
                .where(follow.followee.id.eq(myId)).fetchOne();
    }

    @Override
    public Long countMyFollowings(long myId) {  // 내가 팔로잉하는 사람(나 : 팔로워)
        return queryFactory
                .select(follow.count())
                .from(follow)
                .where(follow.follower.id.eq(myId)).fetchOne();
    }

    @Override
    public List<FollowerDto> findAllFollowers(long myId) {  // 내가 팔로잉하는 사람(나 : 팔로워)
        return queryFactory
                .select(new QFollowerDto(user))
                .from(follow)
                .leftJoin(follow.followee)
                .leftJoin(follow.followee.profile)
                .where(follow.follower.id.eq(myId)).fetch();
    }

    @Override
    public List<FollowerDto> findAllFollowings(long myId) {  // 나를 팔로잉하는 사람(나 : 팔로이)
        return queryFactory
                .select(new QFollowerDto(user))
                .from(follow)
                .leftJoin(follow.follower)
                .leftJoin(follow.follower.profile)
                .where(follow.followee.id.eq(myId)).fetch();
    }


    @Override
    public boolean existsFollow(Long followerId, Long followeeId) {  // 팔로이의 팔로워인지 확인
        Integer ret = queryFactory
                .selectOne()
                .from(follow)
                .where(follow.follower.id.eq(followerId)
                        .and(follow.followee.id.eq(followeeId)))
                .fetchFirst();
        return ret != null;
    }

    @Override
    public Follow findByFollowerIdAndFolloweeId(Long followerId, Long followeeId) {  // 팔로이의 팔로워인지 확인
        return queryFactory
                .selectFrom(follow)
                .where(follow.followee.id.eq(followeeId)
                        .and(follow.follower.id.eq(followerId))).fetchOne();
    }
}
