package com.bff.example.domain.popular;

import com.bff.example.domain.page.model.Page;
import com.bff.example.domain.section.Section;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static com.bff.example.core.util.ObjectMapperHolder.readValue;

@ApplicationScoped
@Transactional
@RequiredArgsConstructor
public class PopularSectionService extends Section {

    private static final String SECTION_ID = "PRODUCT_POPULARS";

    @Override
    public Map<String, Object> doBuild(Page page) {
        var headSection = page.getSection(SECTION_ID);
        return headSection
            .map(section -> readValue(section.metadata, HashMap.class))
            .orElse(null);
    }

    @Override
    public String id() {
        return SECTION_ID;
    }
}
