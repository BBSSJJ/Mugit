package com.ssafy.mugit.hashtag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "hashtag")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    Long id;

    @Column(name = "hashtag_name")
    String name;

    public Hashtag(String name) {
        this.name = name;
    }
}
