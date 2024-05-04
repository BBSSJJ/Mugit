package com.ssafy.mugit.record.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "source")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Source {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "source_id")
    private Long id;

    @Column(name = "source_path")
    private String path;

    public Source(String path) {
        this.path = path;
    }
}
