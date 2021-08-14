package com.bff.example.controller.rest;

import com.bff.example.application.bff.BffPageService;
import com.bff.example.constants.AuthoritiesConstants;
import com.bff.example.infrastructure.mongo.metadata.PageEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/admin/pages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@RequiredArgsConstructor
public class BffPageResource {
    private final Logger log = LoggerFactory.getLogger(BffPageResource.class);

    private final BffPageService bffPageService;

    /**
     * {@code GET /pages} : get all page entities.
     *
     * @param pagination the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and with body all pages.
     */
    @GET
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public Response getPages(@QueryParam("sort") String pagination) {
        var pages = bffPageService.getAllPages();
        Response.ResponseBuilder response = Response.ok(pages);
        return response.build();
    }

    /**
     * {@code GET /pages/{key}} : Get page by key.
     *
     * @return the {@link Response} with status {@code 200 (OK)} and with body pageEntity BFF.
     */
    @GET
    @Path("/{key}")
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public Response getPageByKey(@PathParam("key") String key) {
        var page = bffPageService.getByKey(key);
        Response.ResponseBuilder response = Response.ok(page);
        return response.build();
    }


    /**
     * {@code PUT /pages} : Updates an existing PageEntity.
     *
     * @param pageEntity the user to update.
     * @return the {@link Response} with status {@code 204 (No Content)} and with empty body.
     */
    @PUT
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public Response updatePage(@Valid PageEntity pageEntity) {
        log.debug("REST request to update PageEntity : {}", pageEntity);

        bffPageService.update(pageEntity);
        return Response.noContent().build();
    }

}
