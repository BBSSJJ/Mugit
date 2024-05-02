package com.ssafy.mugit.user.entity;

import com.ssafy.mugit.user.entity.type.RoleType;
import com.ssafy.mugit.user.entity.type.SnsType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.ssafy.mugit.user.entity.type.RoleType.ROLE_USER;

@Entity(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "sns_id", unique = true)
    private String snsId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "sns_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SnsType snsType;

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RoleType role;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Profile profile;

    @OneToMany(mappedBy = "notified", orphanRemoval = true, cascade = CascadeType.ALL)
    private final List<Notification> notifications = new ArrayList<>();

    // 회원가입 시 생성자
    public User(String snsId, String email, SnsType snsType) {
        this.snsId = snsId;
        this.snsType = snsType;
        this.email = email;
        this.role = ROLE_USER;
    }

    // User - Regist 연관관계 편의 메서드
    public void regist(Profile profile) {
        this.profile = profile;
        profile.regist(this);
    }

    // 가짜 회원가입 시 SNS ID 생성
    public void makeMockSnsId() {
        this.snsId = "Mock_" + this.id;
    }
}