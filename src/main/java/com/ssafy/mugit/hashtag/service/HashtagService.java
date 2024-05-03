package com.ssafy.mugit.hashtag.service;

import com.ssafy.mugit.hashtag.entity.Hashtag;
import com.ssafy.mugit.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public List<Hashtag> update(List<String> hashtags) {
        List<Hashtag> existHashtags = hashtagRepository.findHashtagsInDatabase(hashtags);

        List<Hashtag> newHashtags = new ArrayList<>();
        hashtags.forEach((hashtag) -> {
            boolean exist = false;
            for (Hashtag existHashtag : existHashtags) {
                if (existHashtag.getName().equals(hashtag)){
                    exist = true;
                    break;
                }
            }
            if(!exist){
                newHashtags.add(new Hashtag(hashtag));
            }
        });

        hashtagRepository.saveAll(newHashtags);

        existHashtags.addAll(newHashtags);
        return existHashtags;
    }
}
