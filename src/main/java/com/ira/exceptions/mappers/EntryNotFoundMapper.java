package com.ira.exceptions.mappers;

import com.ira.exceptions.EntryNotFound;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EntryNotFoundMapper implements ExceptionMapper<EntryNotFound> {

    @Override
    public Response toResponse(EntryNotFound exception) {
        return  Response.status(404)
                .entity(exception.getMessage())
                .build();
    }
}
