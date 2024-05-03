package com.ssafy.mugit.hashtag.repository;

import com.ssafy.mugit.hashtag.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    @Query("SELECT h FROM hashtag h WHERE h.name in :hashtags")
    List<Hashtag> findHashtagsInDatabase(List<String> hashtags);
}
