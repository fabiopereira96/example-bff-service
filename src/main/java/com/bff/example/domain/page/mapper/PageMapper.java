package com.bff.example.domain.page.mapper;

import com.bff.example.domain.page.model.Page;
import com.bff.example.domain.page.model.Section;
import com.bff.example.infrastructure.mongo.metadata.PageEntity;
import com.bff.example.infrastructure.mongo.metadata.SectionEntity;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "cdi")
public interface PageMapper {

    Page entityToPage(PageEntity pageEntity);

    Section entityToSection(SectionEntity sectionEntity);

    default List<Section> emptySection(Set<String> sections){
        return new ArrayList<>();
    }
}
