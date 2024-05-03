package com.ssafy.mugit.flow.main.service;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.entity.FlowHashtag;
import com.ssafy.mugit.flow.main.repository.FlowHashtagRepository;
import com.ssafy.mugit.hashtag.entity.Hashtag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlowHashtagService {
    private final FlowHashtagRepository flowHashtagRepository;

    void addHashtags(Flow flow, List<Hashtag> hashtags) {
        List<FlowHashtag> newFlowHashtags = new ArrayList<>();
        hashtags.forEach((hashtag) -> {
            newFlowHashtags.add(new FlowHashtag(flow, hashtag));
        });
        flowHashtagRepository.saveAll(newFlowHashtags);
    }
}
