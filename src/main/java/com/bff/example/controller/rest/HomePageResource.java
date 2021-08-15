package com.bff.example.controller.rest;

import com.bff.example.application.home.HomeService;
import com.bff.example.controller.rest.exception.UserNotAuthenticatedException;
import com.bff.example.infrastructure.mongo.user.UserEntity;
import io.quarkus.security.Authenticated;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Optional;

@Path("/api/homepage")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@RequiredArgsConstructor
public class HomePageResource {
    private final Logger log = LoggerFactory.getLogger(HomePageResource.class);

    private static final String HOME_PAGE_KEY = "HOME_PAGE_DELIVERY";

    private final HomeService homeService;

    /**
     * {@code GET /homepage} : Get home page app.
     *
     * @return the {@link Response} with status {@code 200 (OK)} and with body homepage BFF.
     */
    @GET
    @Authenticated
    public Response getHomePage(@Context SecurityContext securityContext) {
        var userLogin = Optional
            .ofNullable(securityContext.getUserPrincipal().getName())
            .orElseThrow(UserNotAuthenticatedException::new);
        var user = UserEntity.findOneByLogin(userLogin);

        if (user.isEmpty()) {
            throw new UserNotAuthenticatedException("UserEntity could not be found");
        } else {
            return getResponseHomePage(user.get());
        }
    }

    private Response getResponseHomePage(UserEntity user) {
        var homePage = homeService.getHomePage(HOME_PAGE_KEY, user.id);
        Response.ResponseBuilder response = Response.ok(homePage);
        return response.build();
    }

}
