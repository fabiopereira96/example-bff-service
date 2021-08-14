package com.bff.example.domain.head;

import com.bff.example.domain.page.model.Page;
import com.bff.example.domain.section.Section;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Map;

import static com.bff.example.core.util.MustacheFactory.buildAndReplace;

@ApplicationScoped
@Transactional
@RequiredArgsConstructor
public class HeadSectionService extends Section {

    private static final String SECTION_ID = "HOME_PAGE_HEAD";

    @Override
    public Map<String, Object> doBuild(Page page) {
        var headSection = page.getSection(SECTION_ID);
        return headSection
            .map(section -> buildAndReplace(section.metadata, this.findData(page)))
            .orElse(null);
    }

    @Override
    public String id() {
        return SECTION_ID;
    }

    private HeadSection findData(Page page) {
        //TODO Find data by external client
        var histories = new ArrayList<History>();
        histories.add(new History("Pizza"));
        histories.add(new History("Pizza Grande"));
        histories.add(new History("Pizzaria Ze"));

        return HeadSection.builder()
            .customerPicture("213313231873434.png")
            .histories(histories)
            .order(page.getOrderSection(SECTION_ID))
            .build();
    }
}
