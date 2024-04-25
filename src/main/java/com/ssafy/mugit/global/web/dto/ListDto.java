package com.ssafy.mugit.global.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ListDto<T> {
    private List<T> list;
}
