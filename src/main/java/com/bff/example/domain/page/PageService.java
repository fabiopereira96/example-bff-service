package com.bff.example.domain.page;

import com.bff.example.domain.page.mapper.PageMapper;
import com.bff.example.domain.page.model.Page;
import com.bff.example.infrastructure.mongo.metadata.PageEntity;
import com.bff.example.infrastructure.mongo.metadata.SectionEntity;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

import static com.bff.example.infrastructure.mongo.metadata.PageEntity.findByIdOptional;
import static com.bff.example.infrastructure.mongo.metadata.SectionEntity.findByListIds;
import static java.util.Optional.*;

@ApplicationScoped
@Transactional
@RequiredArgsConstructor
public class PageService {

    private final PageMapper mapper;

    public Page getById(String id) throws Exception {
        var optionalPageEntity = findByIdOptional(id);
        return optionalPageEntity
            .map(this::getPageSections)
            .orElseThrow(Exception::new);
    }

    private Page getPageSections(PageEntity pageEntity) {
        var page = mapper.entityToPage(pageEntity);
        return ofNullable(pageEntity.sections)
            .map(existsSections -> getConvertedSections(page, pageEntity))
            .orElse(page);
    }

    private Page getConvertedSections(Page page, PageEntity pageEntity) {
        var sectionEntities = findByListIds(pageEntity.getSections());
        return ofNullable(sectionEntities)
            .map(nonNullSections -> appendConvertedSectionsInPage(page, nonNullSections))
            .orElse(page);
    }

    private Page appendConvertedSectionsInPage(Page page, List<SectionEntity> sectionEntities) {
        sectionEntities.forEach(sectionEntity -> page.sections.add(mapper.entityToSection(sectionEntity)));
        return page;
    }

}
