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
    public Long countMyFollowers(long myId) {  // 내가 팔로우하는 사람(나 : 팔로잉)
        return queryFactory
                .select(follow.count())
                .from(follow)
                .where(follow.following.id.eq(myId)).fetchOne();
    }

    @Override
    public Long countMyFollowings(long myId) {  // 나를 팔로우하는 사람(나 : 팔로이)
        return queryFactory
                .select(follow.count())
                .from(follow)
                .where(follow.followee.id.eq(myId)).fetchOne();
    }

    @Override
    public List<FollowerDto> findAllFollowers(long myId) {  // 내가 팔로우하는 사람(나 : 팔로잉)
        return queryFactory
                .select(new QFollowerDto(user))
                .from(follow)
                .leftJoin(follow.followee)
                .leftJoin(follow.followee.profile)
                .where(follow.following.id.eq(myId)).fetch();
    }

    @Override
    public List<FollowerDto> findAllFollowings(long myId) {  // 나를 팔로우하는 사람(나 : 팔로이)
        return queryFactory
                .select(new QFollowerDto(user))
                .from(follow)
                .leftJoin(follow.following)
                .leftJoin(follow.following.profile)
                .where(follow.followee.id.eq(myId)).fetch();
    }


    @Override
    public boolean existsFollow(Long followingId, Long followeeId) {  // 팔로이를 팔로잉하는지 확인
        Integer ret = queryFactory
                .selectOne()
                .from(follow)
                .where(follow.following.id.eq(followingId)
                        .and(follow.followee.id.eq(followeeId)))
                .fetchFirst();
        return ret != null;
    }

    @Override
    public Follow findByFollowingIdAndFolloweeId(Long followingId, Long followeeId) {  // 팔로이를 팔로잉하는지 확인
        return queryFactory
                .selectFrom(follow)
                .where(follow.followee.id.eq(followeeId)
                        .and(follow.following.id.eq(followingId))).fetchOne();
    }
}
