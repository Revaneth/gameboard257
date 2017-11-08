package com.ira.exceptions.mappers;

import com.ira.exceptions.ErrorMessage;
import com.ira.exceptions.InternalServerError;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InternalServerErrorMapper implements ExceptionMapper<InternalServerError> {

    private ErrorMessage createEntity(Exception exception) {
        return new ErrorMessage("Internal server error");
    }

    @Override
    public Response toResponse(InternalServerError exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(createEntity(exception))
                .build();
    }
}