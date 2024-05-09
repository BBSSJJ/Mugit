package com.ssafy.mugit.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Transient
    private final String DEFAULT_PROFILE_TEXT = "텍스트를 입력하세요.";

    @Transient
    private final String DEFAULT_PROFILE_IMAGE_PATH = "https://mugit.site/files/default/user.jpg";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "profile_id")
    private Long id;

    @Column(name = "nick_name", nullable = false, unique = true)
    private String nickName;

    @Column(name = "profile_text", nullable = false)
    private String profileText;

    @Column(name = "profile_image_path", nullable = false)
    private String profileImagePath;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 회원가입 시 생성자(기본값 설정)
    public Profile(String nickName, String profileText, String profileImagePath) {
        this.nickName = nickName;
        this.profileText = !profileText.isBlank() ? profileText : DEFAULT_PROFILE_TEXT;
        this.profileImagePath = !profileImagePath.isBlank() ? profileImagePath : DEFAULT_PROFILE_IMAGE_PATH;
    }

    // Fixture 생성자
    public Profile(Long id, String nickName, String profileText, String profileImagePath, User user) {
        this.id = id;
        this.nickName = nickName;
        this.profileText = !profileText.isBlank() ? profileText : DEFAULT_PROFILE_TEXT;
        this.profileImagePath = !profileImagePath.isBlank() ? profileImagePath : DEFAULT_PROFILE_IMAGE_PATH;
    }

    // Regist - User 연관관계 편의 메서드
    public void regist(final User user) {
        this.user = user;
    }

    // 사용자 업데이트(기본값 설정)
    public void update(String nickName, String profileText, String profileImagePath) {
        this.nickName = nickName;
        this.profileText = !profileText.isBlank() ? profileText : DEFAULT_PROFILE_TEXT;
        this.profileImagePath = !profileImagePath.isBlank() ? profileImagePath : DEFAULT_PROFILE_IMAGE_PATH;
    }
}

