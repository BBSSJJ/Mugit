package com.ssafy.mugit.record.entity;

import com.ssafy.mugit.record.entity.Record;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity(name = "source")
@Getter
@Builder
@AllArgsConstructor
public class Source {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "source_id")
    Long id;

    @Column(name = "original_name")
    String originName;

    @Column(name = "uuid_name")
    String uuidName;
}
