package com.bff.example.application.home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomePage implements Serializable {
    private static final long serialVersionUID = -7738700441153511385L;

    private String id;
    private String status;
    private HomePageSection sections;
}
