package com.ssafy.mugit.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {
    @Transient
    private final String DEFAULT_PROFILE_TEXT = "텍스트를 입력하세요.";
    @Transient
    private final String DEFAULT_PROFILE_IMAGE_PATH = "DEFAULT_IMAGE_URL";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "profile_text")
    private String profileText;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 회원가입 시 프로필 생성자
    public Profile(String nickName, String profileText, String profileImagePath) {
        this.nickName = nickName;
        this.profileText = !profileText.isBlank() ? profileText : DEFAULT_PROFILE_TEXT;
        this.profileImagePath = !profileImagePath.isBlank() ? profileImagePath : DEFAULT_PROFILE_IMAGE_PATH;
    }

    // 기본 생성자
    public Profile(Long id, String nickName, String profileText, String profileImagePath, User user) {
        this.id = id;
        this.nickName = nickName;
        this.profileText = !profileText.isBlank() ? profileText : DEFAULT_PROFILE_TEXT;
        this.profileImagePath = !profileImagePath.isBlank() ? profileImagePath : DEFAULT_PROFILE_IMAGE_PATH;
    }

    public void regist(final User user) {
        this.user = user;
    }
}

