package com.bff.example.controller.util;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

public interface ResponseUtil {
    static Response wrapOrNotFound(Optional maybeResponse) {
        return wrapOrNotFound(maybeResponse, Collections.emptyMap());
    }

    static Response wrapOrNotFound(Optional maybeResponse, Map<String, String> header) {
        Response.ResponseBuilder response = (Response.ResponseBuilder) maybeResponse
            .map(Response::ok)
            .orElse(Response.status(NOT_FOUND));
        header.forEach(response::header);
        return response.build();
    }
}
