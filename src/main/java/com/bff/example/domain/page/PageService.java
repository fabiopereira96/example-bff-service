package com.bff.example.domain.page;

import com.bff.example.domain.page.mapper.PageMapper;
import com.bff.example.domain.page.model.Page;
import com.bff.example.infrastructure.mongo.metadata.PageEntity;
import com.bff.example.infrastructure.mongo.metadata.SectionEntity;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Consumer;

import static com.bff.example.infrastructure.mongo.metadata.PageEntity.*;
import static com.bff.example.infrastructure.mongo.metadata.SectionEntity.findByListKeys;
import static java.util.Optional.ofNullable;

@ApplicationScoped
@Transactional
@RequiredArgsConstructor
public class PageService {

    private final PageMapper mapper;

    public Page getById(String id) throws Exception {
        var optionalPageEntity = PageEntity.findByKeyOptional(id);
        return optionalPageEntity
            .map(this::getPageSections)
            .orElseThrow(Exception::new);
    }

    private Page getPageSections(PageEntity pageEntity) {
        var page = mapper.entityToPage(pageEntity);
        return ofNullable(pageEntity.sections)
            .map(sectionsByPage -> getConvertedSections(pageEntity, page, sectionsByPage))
            .orElse(page);
    }

    private Page getConvertedSections(PageEntity pageEntity, Page page, List<SectionKey> sectionsByPage) {
        var sectionEntities = findByListKeys(sectionsByPage);
        return ofNullable(sectionEntities)
            .map(existsSections -> appendAndOrderConvertedSectionsInPage(pageEntity, page, existsSections))
            .orElse(page);
    }

    private Page appendAndOrderConvertedSectionsInPage(PageEntity pageEntity, Page page, List<SectionEntity> sectionEntities) {
        sectionEntities.forEach(appendSectionWithOrder(pageEntity, page));
        return page;
    }

    private Consumer<SectionEntity> appendSectionWithOrder(PageEntity pageEntity, Page page) {
        return sectionEntity -> page.sections.add(
            mapper.entityToSection(sectionEntity,
                pageEntity.getSectionOrderByKey(sectionEntity.getKey())));
    }

}
