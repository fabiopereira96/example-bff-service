package com.bff.example.domain.page.model;

import java.util.List;
import java.util.Optional;

public class Page {
    public String type;
    public String key;
    public String status;
    public List<Section> sections;
    public String title;
    public String so;
    public String userId;

    public Optional<Section> getSection(String sectionId) {
        return this.sections.stream().filter(section -> sectionId.equals(section.key))
            .findFirst();
    }

    public int getOrderSection(String sectionId) {
        return getSection(sectionId)
            .map(section -> section.order)
            .orElse(Integer.MAX_VALUE);
    }
}
