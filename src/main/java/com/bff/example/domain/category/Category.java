package com.bff.example.domain.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Category {
    private final String name;
    private final String icon;
    private final String id;
}
