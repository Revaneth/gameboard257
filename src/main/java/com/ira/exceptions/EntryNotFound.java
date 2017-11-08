package com.ira.exceptions;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import java.util.UUID;

public class EntryNotFound extends ClientErrorException {

    public EntryNotFound(UUID id) {
        super("Entry with given UUID : " + id+
                " not found ", Response.Status.NOT_FOUND);
    }
}
