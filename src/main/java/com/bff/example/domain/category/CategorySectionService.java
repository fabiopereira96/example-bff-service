package com.bff.example.domain.category;

import com.bff.example.domain.page.model.Page;
import com.bff.example.domain.section.Section;
import com.bff.example.infrastructure.mongo.metadata.category.CategoryMetadataSection;
import com.bff.example.infrastructure.mongo.metadata.category.CategorySection;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.*;

import static com.bff.example.core.util.MustacheFactory.buildAndReplace;
import static com.bff.example.core.util.ObjectMapperHolder.readValue;
import static com.google.common.collect.Iterables.isEmpty;
import static java.util.Arrays.asList;

@ApplicationScoped
@Transactional
@RequiredArgsConstructor
public class CategorySectionService extends Section {

    private static final String SECTION_ID = "PRODUCT_CATEGORIES";
    private static final String PROPERTY_TITLE = "title";
    private static final String PROPERTY_LIST_CATEGORY = "items";

    @Override
    public Map<String, Object> doBuild(Page page) {
        var categoriesMetadataSection = getCategoriesMetadataSection(page);
        var categories = findData();
        if (categoriesMetadataSection.isEmpty() || isEmpty(categories)) {
            return null;
        }
        return buildCategories(categories, categoriesMetadataSection.get());
    }

    @Override
    public String id() {
        return SECTION_ID;
    }

    private Map<String, Object> buildCategories(List<Category> categories,
                                                CategoryMetadataSection categoriesMetadataSection) {
        var finalCategories = new HashMap<String, Object>();
        var convertedCategory = new ArrayList<CategorySection>();

        finalCategories.put(PROPERTY_TITLE, categoriesMetadataSection.getTitle());
        categories.forEach(category -> convertedCategory.add(buildAndReplace(categoriesMetadataSection.getCategoriesProperties(), category, CategorySection.class)));
        finalCategories.put(PROPERTY_LIST_CATEGORY, convertedCategory);

        return finalCategories;
    }

    private Optional<CategoryMetadataSection> getCategoriesMetadataSection(Page page) {
        var categoriesSection = page.getSection(SECTION_ID);
        return categoriesSection
            .map(section -> readValue(section.metadata, CategoryMetadataSection.class));
    }

    private List<Category> findData() {
        //TODO Find data by external client
        var category1 = new Category("Pizza", "PIZZA", "102020238239238");
        var category2 = new Category("Seafood", "PRAWN", "8947547654059740");
        var category3 = new Category("Soft Drinks", "SOFT_DRINK", "19287283692434237403");
        return asList(category1, category2, category3);
    }
}
