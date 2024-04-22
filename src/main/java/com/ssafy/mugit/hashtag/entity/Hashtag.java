package com.ssafy.mugit.hashtag.entity;

import jakarta.persistence.*;

@Entity(name = "hashtag")
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    Long id;

    @Column(name = "hashtag_name")
    String name;
}
