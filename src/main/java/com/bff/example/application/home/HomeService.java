package com.bff.example.application.home;

import com.bff.example.core.exception.HomePageException;
import com.bff.example.domain.category.CategorySectionService;
import com.bff.example.domain.head.HeadSectionService;
import com.bff.example.domain.page.PageService;
import com.bff.example.domain.popular.PopularSectionService;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
@RequiredArgsConstructor
public class HomeService {

    private final PageService pageService;
    private final HeadSectionService headService;
    private final CategorySectionService categoriesService;
    private final PopularSectionService popularService;

    public HomePage getHomePage(String id) {
        return doGetHomePage(id);
    }

    private HomePage doGetHomePage(String id) {
        try {
            var page = pageService.getById(id);
            return HomePage.builder()
                .id(page.key)
                .status(page.status)
                .sections(HomePageSection.builder()
                    .head(headService.build(page))
                    .categories(categoriesService.build(page))
                    .populars(popularService.build(page))
                    .build())
                .build();

        } catch (Exception exception) {
            throw new HomePageException(exception);
        }
    }

}
