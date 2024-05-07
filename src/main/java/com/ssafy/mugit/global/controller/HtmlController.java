package com.ssafy.mugit.global.controller;

import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HtmlController {

    private final UserRepository userRepository;

    @GetMapping("/")
    String index() {
        return "/index";
    }

    @GetMapping("/login")
    String login() {
        return "/login";
    }

    @GetMapping("/sns-login")
    String snsLogin() {
        return "/sns-login";
    }

    @GetMapping("/sns-regist")
    String snsRegist() {
        return "/sns-regist";
    }

    @GetMapping("/manage-user")
    String manageUser(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "/manage-user";
    }

    @GetMapping("/sse")
    String sse() {
        return "/sse";
    }
}
