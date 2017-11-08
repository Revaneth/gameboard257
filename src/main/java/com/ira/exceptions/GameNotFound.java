package com.ira.exceptions;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import java.util.UUID;


public class GameNotFound extends ClientErrorException {

    public GameNotFound(String title) {
        super("Game with given title: " + title+
                " not found ", Response.Status.NOT_FOUND);
    }

    public GameNotFound(UUID id) {
        super("Game with given UUID : " + id+
                " not found ", Response.Status.NOT_FOUND);
    }

}
