package com.ira.exceptions.mappers;

import com.ira.exceptions.ErrorMessage;
import com.ira.exceptions.UserSideException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class UserSideExceptionMapper implements ExceptionMapper<UserSideException> {

    @Override
    public Response toResponse(UserSideException exception) {
      return   Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorMessage(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}