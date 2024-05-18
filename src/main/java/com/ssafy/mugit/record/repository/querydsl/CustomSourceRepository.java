package com.ssafy.mugit.record.repository.querydsl;

import com.ssafy.mugit.record.entity.Source;

import java.util.List;

public interface CustomSourceRepository {
    List<Source> findNotUsed();
}
