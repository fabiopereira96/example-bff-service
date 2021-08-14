package com.bff.example.controller.rest;

import com.bff.example.application.home.HomeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/homepage")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@RequiredArgsConstructor
public class HomePageResource {
    private final Logger log = LoggerFactory.getLogger(HomePageResource.class);

    private static final String HOME_PAGE_ID = "HOME_PAGE_DELIVERY";

    private final HomeService homeService;

    /**
     * {@code GET /homepage} : Get home page app.
     *
     * @return the {@link Response} with status {@code 200 (OK)} and with body homepage BFF.
     */
    @GET
    public Response getAllUsers() {
        var homePage = homeService.getHomePage(HOME_PAGE_ID);
        Response.ResponseBuilder response = Response.ok(homePage);
        return response.build();
    }

}
