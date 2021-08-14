package com.bff.example.domain.page.mapper;

import com.bff.example.domain.page.model.Page;
import com.bff.example.domain.page.model.Section;
import com.bff.example.infrastructure.mongo.metadata.PageEntity;
import com.bff.example.infrastructure.mongo.metadata.PageEntity.SectionKey;
import com.bff.example.infrastructure.mongo.metadata.SectionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "cdi")
public interface PageMapper {

    Page entityToPage(PageEntity pageEntity);

    Section entityToSection(SectionEntity sectionEntity, int order);

    default List<Section> emptySection(List<SectionKey> sections){
        return new ArrayList<>();
    }
}
