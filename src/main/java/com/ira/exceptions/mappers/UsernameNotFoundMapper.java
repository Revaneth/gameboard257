package com.ira.exceptions.mappers;

import com.ira.exceptions.UserNotFound;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UsernameNotFoundMapper implements ExceptionMapper<UserNotFound> {

   @Override
    public Response toResponse(UserNotFound exception) {
        return  Response.status(404)
                .entity(exception.getMessage())
                .build();
    }
}