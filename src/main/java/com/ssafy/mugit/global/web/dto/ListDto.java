package com.ssafy.mugit.global.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListDto<T> {
    private T list;
}
