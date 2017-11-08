package com.ira.exceptions.mappers;

import com.ira.exceptions.GameNotFound;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GameNotFoundMapper implements ExceptionMapper<GameNotFound> {

    @Override
    public Response toResponse(GameNotFound exception) {
        return  Response.status(404)
                .entity(exception.getMessage())
                .build();
    }
}