package com.bff.example.domain.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Product {

    private long id;
    private String name;
    private String description;
    private String rating;
    private String tagDescription;
}
