package com.bff.example.domain.section;

import com.bff.example.domain.page.model.Page;

import java.util.Map;

import static java.util.Objects.nonNull;

public abstract class Section {
    private static final String PROPERTY_ORDER = "order";

    public Map<String, Object> build(Page page) {
        var properties = doBuild(page);
        if(nonNull(properties)) {
            properties.put(PROPERTY_ORDER, page.getOrderSection(id()));
        }
        return properties;
    }

    public abstract Map<String, Object> doBuild(Page page);
    public abstract String id();
}
