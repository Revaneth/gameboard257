package com.ira.api;

import com.ira.database.MainDatabase;
import com.ira.database.PostgresqlDB;
import com.ira.entity.GameEntity;
import com.ira.exceptions.GameNotFound;
import com.ira.exceptions.InternalServerError;
import com.ira.model.Game;
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
@Api(value = "games", description = "the games API")
public class   GamesApi {

    private static final MainDatabase database = new PostgresqlDB();
    private static Logger logger = LoggerFactory.getLogger(GamesApi.class);
    private static MainDatabase getDatabase() {
        return database;
    }

    @RequestMapping(value = "/games",
            produces = {"application/json", "application/xml"},
            method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation")})
    @ApiOperation(value = "Get games collection", notes = "Get games collection, might be empty list",
            response = Game.class, responseContainer = "LIST", tags = {"game"})
    public Collection<Game> list() {
        return getDatabase().getGames();
    }

    @RequestMapping(value = "/games/{title}", produces = {"application/json", "application/xml"},
            method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Game.class),
            @ApiResponse(code = 404, message = "Bad title /Game not found", response = Game.class)})
    @ApiOperation(value = "Get game by title", notes = "Get games ",
            response = Game.class, responseContainer = "LIST", tags = {"game"})
    public ResponseEntity getGameSByTitle(@PathVariable("title") String title) {
        try {
            if (getDatabase().getGamesByTitle(title).isEmpty())
                throw new GameNotFound(title);
            return ResponseEntity.ok().body(getDatabase().getGamesByTitle(title));
        } catch (GameNotFound g) {
            return ResponseEntity.status(404).body(g.getMessage());
        }
    }

    @ApiOperation(value = "Find game by ID",
            response = Game.class, tags = {"game"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Game.class),
            @ApiResponse(code = 404, message = "Bad UUID format/Game not found", response = Game.class)})
    @RequestMapping(value = "/games/id/{gameId}",
            produces = {"application/json", "application/xml"},
            method = RequestMethod.GET)
    ResponseEntity getGameById(@ApiParam(value = "ID of game that needs to be fetched", required = true)
                               @PathVariable("gameId") String gameId)
            throws Exception {
        try {
            Game game = getDatabase().getGame(UUID.fromString(gameId));
            if (gameId.equals("db")) {
                throw new InternalServerError(
                        "Database problem", Response.Status.INTERNAL_SERVER_ERROR);
            }
            if (game == null) {
                throw new GameNotFound(UUID.fromString(gameId));
            }
            return ResponseEntity.ok().body(game);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Add a new game to the store", response = Void.class,
            tags = {"game",})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid input", response = Void.class)})
    @RequestMapping(value = "/games",
            produces = {"application/json", "application/xml"},
            consumes = {"application/json", "application/xml"},
            method = RequestMethod.POST)
    public ResponseEntity addGame(@ApiParam(value = "Game object that needs to be added to the store")
                                  @RequestBody Game game) throws Exception {
        try {      Game dbGame = new Game(
                    game.getTitleOfGame(),
                    game.getTypeOfGame());
            GameEntity createdGame = getDatabase().createGame(dbGame);
            return ResponseEntity.created(URI.create("games" + "/" + createdGame.getTitle())).body(createdGame);
        } catch (Exception d) {
            return ResponseEntity.badRequest()
                    .body("Invalid input");
        }
    }

    @ApiOperation(value = "Deletes a game", response = Void.class, tags = {"game",})
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Game wit given id not found", response = Void.class)})
    @RequestMapping(value = "/games/id/{gameId}",
            produces = {"application/json", "application/xml"},
            method = RequestMethod.DELETE)
    ResponseEntity deleteGame(
            @ApiParam(value = "Game id to delete", required = true)
            @PathVariable("gameId") String gameId)
            throws Exception {
        try {
            int t = getDatabase().deleteGame(UUID.fromString(gameId));
            if (t == 0) {
                logger.info("In  'if' after receiving: ");
                return ResponseEntity.ok().body("Game deleted");
            } else {
                throw new GameNotFound(gameId);
            }
        } catch (Exception x) {
            return ResponseEntity.badRequest().body(x.getMessage());
        }
    }

    @ApiOperation(value = "Update an existing game", response = Void.class, tags = {"game",})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid ID supplied", response = Void.class),
            @ApiResponse(code = 404, message = "Game wit given id not found", response = Void.class)})
    @RequestMapping(value = "/games/id/{gameId}",
            produces = {"application/json", "application/xml"},
            consumes = {"application/json", "application/xml"},
            method = RequestMethod.PUT)
    ResponseEntity updateGame(@ApiParam(value = "id of game that need to be changed", required = true)
                              @PathVariable("gameId") String gameId,
                              @ApiParam(value = "Updated game object", required = true)
                              @RequestBody Game body)
            throws Exception {
        try {
            Game temp = getDatabase().getGame(UUID.fromString(gameId));
            if (temp == null) {
                logger.info("game not found");
                throw new GameNotFound(UUID.fromString(gameId));
            }
            String title, type;
            title = (body.getTitleOfGame() == null) ? temp.getTitleOfGame() : body.getTitleOfGame();
            type = (body.getTypeOfGame() == null) ? temp.getTypeOfGame() : body.getTypeOfGame();
            Game newGame = new Game(temp.getGameId(), title, type);
            GameEntity updatedGame = getDatabase().updateGame(newGame);
            return ResponseEntity.ok(updatedGame);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}//class end