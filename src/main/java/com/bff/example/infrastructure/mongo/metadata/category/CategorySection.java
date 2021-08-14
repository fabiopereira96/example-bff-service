package com.bff.example.infrastructure.mongo.metadata.category;

import com.bff.example.infrastructure.mongo.metadata.Button;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategorySection {
    private String name;
    private String icon;
    private Boolean isSelected;
    private Button buttonWithUrl;
}
