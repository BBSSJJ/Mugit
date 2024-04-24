package com.ssafy.mugit.user.entity;

import com.ssafy.mugit.user.entity.type.SnsType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "sns_id")
    private String snsId;

    @Column(name = "sns_type")
    @Enumerated(value = EnumType.STRING)
    private SnsType snsType;

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private Profile profile;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private final List<Notification> notifications = new ArrayList<>();

    // 회원가입 시 fake user 생성자
    public User(String snsId, SnsType snsType, String email) {
        this.snsId = snsId;
        this.snsType = snsType;
        this.email = email;
    }

    public void regist(Profile profile) {
        this.profile = profile;
        profile.regist(this);
    }
}