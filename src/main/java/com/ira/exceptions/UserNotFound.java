package com.ira.exceptions;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import java.util.UUID;

public class UserNotFound extends ClientErrorException {

    public UserNotFound(String name) {
        super("User with given username: " + name+
                " not found ", Response.Status.NOT_FOUND);
    }

    public UserNotFound(UUID id) {
        super("User with given UUID : " + id+
                " not found ", Response.Status.NOT_FOUND);
    }

}
