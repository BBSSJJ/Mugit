package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.util.AcceptanceTestExecutionListener;
import com.ssafy.mugit.user.repository.ProfileRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.util.CookieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;

@Tag("regist")
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class UserRegistServiceTest {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserRepository userRepository;

    CookieUtil cookieUtil;

    UserRegistService sut;

    @BeforeEach
    void setUp() {
        cookieUtil = new CookieUtil();
        sut = new UserRegistService(userRepository, profileRepository, cookieUtil);
    }
}