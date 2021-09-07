package com.bff.example.infrastructure.mongo.metadata.popular;

import com.bff.example.infrastructure.mongo.metadata.Button;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PopularSection {
    private Tag tag;
    private String name;
    private String description;
    private String rating;
    private String image;
    private Boolean isSelected;
    private Button buttonWithUrl;
}
