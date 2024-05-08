package com.ssafy.mugit.flow.likes.service;

import com.ssafy.mugit.flow.likes.entity.Likes;
import com.ssafy.mugit.flow.likes.repository.LikesRepository;
import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final FlowRepository flowRepository;

    public String changeLikes(Long userId, Long flowId) {
        User user = userRepository.getReferenceById(userId);
        Flow flow = flowRepository.getReferenceById(flowId);
        Optional<Likes> likesOptional = likesRepository.findLikeByUserIdAndFlowId(userId, flowId);

        if (likesOptional.isPresent()) {
            likesRepository.delete(likesOptional.get());
            return "좋아요 취소 성공";
        }

        Likes likes = new Likes(user, flow);
        likesRepository.save(likes);
        return "좋아요 등록 성공";
    }
}