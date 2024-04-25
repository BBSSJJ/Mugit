package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/nick/{nickName}")
    public ResponseEntity<ResponseUserProfileDto> findByNick(@PathVariable(name = "nickName") String nickName) {
        log.info("find user: {}", nickName);
        ResponseUserProfileDto dto = userService.findUserByNickName(nickName);
        return ResponseEntity.ok().body(dto);
    }
}
