package com.bff.example.domain.head;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeadSection {

    private String customerPicture;
    private List<History> histories;
    private int order;

    public String getCustomerPicture() {
        return customerPicture;
    }

    public int getOrder() {
        return order;
    }

    public String getHistories() {
        if(isNull(this.histories)){
            return null;
        }
        return this.histories.stream()
            .map(History::getText)
            .collect(Collectors.joining(", "));
    }
}
