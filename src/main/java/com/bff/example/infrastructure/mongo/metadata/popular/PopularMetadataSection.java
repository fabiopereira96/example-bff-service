package com.bff.example.infrastructure.mongo.metadata.popular;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PopularMetadataSection {
    private String title;
    private PopularSection popularsProperties;
}
