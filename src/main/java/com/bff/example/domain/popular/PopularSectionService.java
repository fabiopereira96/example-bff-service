package com.bff.example.domain.popular;

import com.bff.example.domain.page.model.Page;
import com.bff.example.domain.product.Product;
import com.bff.example.domain.section.Section;
import com.bff.example.infrastructure.mongo.metadata.popular.PopularMetadataSection;
import com.bff.example.infrastructure.mongo.metadata.popular.PopularSection;
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
public class PopularSectionService extends Section {

    private static final String SECTION_ID = "PRODUCT_POPULARS";
    private static final String PROPERTY_TITLE = "title";
    private static final String PROPERTY_LIST_PRODUCTS = "items";

    @Override
    public Map<String, Object> doBuild(Page page) {
        var popularMetadataSection = getPopularsMetadataSection(page);
        var products = findData();
        if (popularMetadataSection.isEmpty() || isEmpty(products)) {
            return null;
        }
        return buildPopulars(products, popularMetadataSection.get());
    }

    @Override
    public String id() {
        return SECTION_ID;
    }

    private Map<String, Object> buildPopulars(List<Product> products,
                                              PopularMetadataSection popularMetadataSection) {
        var finalProductsPopular = new HashMap<String, Object>();
        var convertedPopular = new ArrayList<PopularSection>();

        finalProductsPopular.put(PROPERTY_TITLE, popularMetadataSection.getTitle());
        products.forEach(product -> convertedPopular.add(buildAndReplace(popularMetadataSection.getPopularsProperties(), product, PopularSection.class)));
        finalProductsPopular.put(PROPERTY_LIST_PRODUCTS, convertedPopular);

        return finalProductsPopular;
    }

    private Optional<PopularMetadataSection> getPopularsMetadataSection(Page page) {
        var categoriesSection = page.getSection(SECTION_ID);
        return categoriesSection
            .map(section -> readValue(section.metadata, PopularMetadataSection.class));
    }

    private List<Product> findData() {
        //TODO Find data by external client
        var product1 = new Product(10202023, "PIZZA", "Borda recheada", "3", "Melhor da semana");
        var product2 = new Product(10202023, "PASTA", "Macarrão bolonhesa", "2", "Mais pedido hoje");
        var product3 = new Product(10202023, "HAMBURGER", "Combo duplo", "5", "Pra quem gosta de hamburger");
        return asList(product1, product2, product3);
    }
}
