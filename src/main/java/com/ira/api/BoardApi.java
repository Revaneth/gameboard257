package com.ira.api;

import com.ira.database.MainDatabase;
import com.ira.database.PostgresqlDB;
import com.ira.entity.AltEntryEntity;
import com.ira.entity.EntryEntity;
import com.ira.exceptions.EntryNotFound;
import com.ira.exceptions.GameNotFound;
import com.ira.exceptions.InternalServerError;
import com.ira.exceptions.UserSideException;
import com.ira.model.Entry;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collection;
import java.util.UUID;


@RestController
@Api(value = "board", description = "the entries API")
public class BoardApi {

    private static Logger logger = LoggerFactory.getLogger(BoardApi.class);
    private static final MainDatabase database = new PostgresqlDB();
    private static MainDatabase getDatabase() {
        return database;
    }

    @ApiImplicitParam
    @RequestMapping(value = "/entries",
            produces =  { "application/vnd.ira.entries-1+json", "application/vnd.ira.entries-1+xml" },
            method = RequestMethod.GET)
    @ApiOperation(value = "Get entries from board 1", notes = "Get entries collection",
            response = Entry.class, responseContainer = "LIST" , tags={ "board" })
    public Collection<Entry> listv1() {
        return getDatabase().getEntries();
    }

@ApiImplicitParam
    @RequestMapping(value = "/entries",
            produces =  { "application/vnd.ira.entries-2+json", "application/vnd.ira.entries-2+xml" },
            method = RequestMethod.GET)
    @ApiOperation(value = "Get entries from board 2", notes = "Get entries collection",
            response = AltEntryEntity.class, responseContainer = "LIST" , tags={ "board" }
    )
    public Collection<AltEntryEntity> listv2() {
        return getDatabase().getEntriesAlt();
    }

    @ApiOperation(value = "Delete entry entry by ID",
                response = Void.class, tags={ "board", })
        @ApiResponses(value = {
                @ApiResponse(code = 400, message = "Invalid ID supplied", response = Void.class),
                @ApiResponse(code = 404, message = "Entry not found", response = Void.class) })
        @RequestMapping(value = "/entries/{entryId}",
                produces = { "application/json", "application/xml" },
                method = RequestMethod.DELETE)
       ResponseEntity deleteEntry(@ApiParam(value = "ID of the entry that needs to be deleted", required = true) 
                                  @PathVariable("entryId") String entryId) {
        try {
            int t = getDatabase().deleteGame(UUID.fromString(entryId));
            if (t == 0) {
                logger.info("in if after reciving: ");
                return ResponseEntity.ok().body("Entry deleted");
            } else {
                throw new EntryNotFound(UUID.fromString(entryId));
            }
        } catch (Exception x) {
            return new ResponseEntity(x.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

        @ApiOperation(value = "Find entry entry by ID", notes = "For valid response try integer IDs with value <= 5 or > 10. Other values will generated exceptions", response = Entry.class, tags={ "board", })
        @ApiResponses(value = {
                @ApiResponse(code = 200, message = "Successful operation", response = Entry.class),
                @ApiResponse(code = 400, message = "Invalid ID supplied", response = Entry.class),
                @ApiResponse(code = 404, message = "Entry not found", response = Entry.class) })
        @RequestMapping(value = "/entries/{entryId}",
                produces = { "application/json", "application/xml" },
                method = RequestMethod.GET)
        ResponseEntity getEntryById(
                @ApiParam(value = "ID of game that needs to be fetched", required = true)
                @PathVariable("entryId") String entryId)
                throws  Exception {
            try {
                Entry entry = getDatabase().getEntry(UUID.fromString(entryId));
                if (entryId.equals("db")) {
                    throw new InternalServerError("Database problem", Response.Status.INTERNAL_SERVER_ERROR);
                }
                if (entry == null) {
                    throw new GameNotFound(UUID.fromString(entryId));
                }
                return ResponseEntity.ok().body(entry);
            }
               catch (Exception e) {
                return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
            }
        }

        @ApiOperation(value = "Place an entry for a game", response = Entry.class, tags={ "board", })
        @ApiResponses(value = {
                @ApiResponse(code = 200, message = "successful operation", response = Entry.class),
                @ApiResponse(code = 400, message = "Invalid Entry", response = Entry.class) })
        @RequestMapping(value = "/entries",
                produces = { "application/json", "application/xml" },
                method = RequestMethod.POST)
        ResponseEntity placeEntry(@ApiParam(value = "entry placed ")
                                  @RequestBody Entry body)
                throws Exception {
            try {
                logger.info(" in try");
                Entry newEntry = new Entry(body.getUserId(), body.getGameId());
                if (newEntry.getUserId() == null || newEntry.getGameId() == null) {
                    throw new UserSideException(" You need to give both UUIDs ");
                }
                if (getDatabase().getEntry(newEntry.getUserId(), newEntry.getGameId()) != null) {
                    throw new UserSideException("U cant add more than one entry per game per user. ");
                }
                if (getDatabase().getUserByID(newEntry.getUserId()) == null) {
                    throw new UserSideException("U cant add non-existent user. ");
                }
                if (getDatabase().getGame(newEntry.getGameId()) == null) {
                    throw new UserSideException("U cant add non-existent game. ");
                }
                EntryEntity createdEntry = getDatabase().createEntry(newEntry);
                return ResponseEntity.created(URI.create
                        ("entries" + "/" + createdEntry.getId()))
                        .body(createdEntry);
            } catch (UserSideException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (Exception d) {
                return ResponseEntity.badRequest().body("Wrong request format");
            }
        }

    }//class end


