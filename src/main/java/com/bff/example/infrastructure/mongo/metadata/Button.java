package com.bff.example.infrastructure.mongo.metadata;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Button {
    private String title;
    private String url;
    private Map<String, Object> params;
}
