package com.ssafy.mugit.user.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ListDto<T> {
    private List<T> list;
}
