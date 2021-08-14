package com.bff.example.application.home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomePageSection implements Serializable {
    private static final long serialVersionUID = -3394824032210571880L;

    private Map<String, Object> head;
    private Map<String, Object> categories;
    private Map<String, Object> populars;
}
