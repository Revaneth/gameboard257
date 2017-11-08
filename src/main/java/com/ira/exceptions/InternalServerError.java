package com.ira.exceptions;

import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class InternalServerError extends ServerErrorException {

    public InternalServerError (String message, Response.Status status){
            super(message, Response.Status.INTERNAL_SERVER_ERROR);}
    }






