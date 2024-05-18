package com.ssafy.mugit.feed.service;

import com.ssafy.mugit.flow.main.dto.FlowItemDto;
import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final UserRepository userRepository;
    private final FlowRepository flowRepository;

    @Transactional(readOnly = true)
    public Slice<FlowItemDto> getFeeds(Pageable pageable, Long userId) {
        if (userId == null) {
            throw new UserApiException(UserApiError.NOT_AUTHORIZED_USER);
        }
        User user = userRepository.getReferenceById(userId);
        List<User> followees = userRepository.findFolloweesByUser(user);

        return flowRepository.findFlowsByUsers(pageable, followees).map(FlowItemDto::new);
    }
}
