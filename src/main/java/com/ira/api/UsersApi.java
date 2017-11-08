package com.ira.api;

import com.ira.database.MainDatabase;
import com.ira.database.PostgresqlDB;
import com.ira.entity.UserEntity;
import com.ira.exceptions.InternalServerError;
import com.ira.exceptions.UserNotFound;
import com.ira.exceptions.UserSideException;
import com.ira.model.User;
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
@Api(value = "users", description = "the users API")
public  class UsersApi {
    private static Logger logger = LoggerFactory.getLogger(UsersApi.class);
    private static final MainDatabase database = new PostgresqlDB();
    private static MainDatabase getDatabase() {
        return database;
    }

    @RequestMapping(value = "/users",
            produces = {"application/json", "application/xml"},
            method = RequestMethod.GET)
    @ApiOperation(value = "Get users collection", notes = "Get users collection",
            response = User.class, responseContainer = "LIST", tags = {"user"})
    public Collection<User> list() {
        return getDatabase().getUsers();
    }

    @ApiOperation(value = "Get user by username", response = User.class, tags = {"user",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = User.class),
            @ApiResponse(code = 400, message = "Invalid username supplied", response = User.class),
            @ApiResponse(code = 404, message = "User with given username not found", response = User.class)})
    @RequestMapping(value = "/users/{username}",
            produces = {"application/json", "application/xml"},
            method = RequestMethod.GET)
    public ResponseEntity getUserByName(@PathVariable("username") String username)
            throws Exception {
        try {
            User user = getDatabase().getUser(username);
            if (username.equals("db")) {
                throw new InternalServerError("Database problem", Response.Status.INTERNAL_SERVER_ERROR);
            }
            if (user == null) {
                throw new UserNotFound(username);
            }
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Get user by ID", response = User.class, tags = {"user",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = User.class),
            @ApiResponse(code = 400, message = "Invalid username supplied", response = User.class),
            @ApiResponse(code = 404, message = "User not found",
                    response = User.class)})
    @RequestMapping(value = "/users/id/{id}",
            produces = {"application/json", "application/xml"},
            method = RequestMethod.GET)
    public ResponseEntity getUserByID(@PathVariable("id") String id)
            throws Exception {
        try {
            User user = getDatabase().getUserByID(UUID.fromString(id));

            if (id.equals("db")) {
                throw new InternalServerError("Database problem", Response.Status.INTERNAL_SERVER_ERROR);
            }

            if (user == null) {
                throw new UserNotFound(UUID.fromString(id));
            }

            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ApiOperation(value = "Create user", notes = "Create user",
            response = User.class, tags = {"user"})
    public ResponseEntity createUser(@RequestBody User user)//, HttpServletRequest request)
            throws Exception {
        try {
            User newUser = new User(
                    user.getUsername(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPassword()
            );
            if (newUser.getUsername() == null) {
                throw new UserSideException(" You need to give at least username ");
            }
            if (getDatabase().getUser(newUser.getUsername()) != null) {
                logger.info("user found");
                throw new UserSideException("User with name:  " + newUser.getUsername() + " already exist");
            }
            UserEntity createdUser = getDatabase().createUser(newUser);
            return ResponseEntity.created(URI.create
                    // (request.getPathInfo()
                            ("users" + "/" + createdUser.getUsername()))
                    .body(createdUser);
        } catch (UserSideException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        } catch (Exception d) {
            return ResponseEntity.badRequest().body("Wrong request format");
        }
    }

    @ApiOperation(value = "Delete user", notes = "This can only be done by the logged in user.",
            response = Void.class, tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid username supplied", response = Void.class),
            @ApiResponse(code = 404, message = "User not found", response = Void.class),
            @ApiResponse(code = 200, message = "Ok", response = Void.class)})
    @RequestMapping(value = "/users/{username}",
            produces = {"application/json", "application/xml"},
            method = RequestMethod.DELETE)
    ResponseEntity deleteUser(@ApiParam(value = "The user by username that needs to be deleted",
            required = true) @PathVariable("username") String username)
            throws Exception {
        Collection<User> users = getDatabase().getUsers();
            try {
            for (User user : users) {
                if (username.equalsIgnoreCase(user.getUsername())) {
                    int t = getDatabase().deleteUser(user.getUserId());
                    if (t == 0) {
                        users.remove(user);
                        logger.info("in if after receiving: ");
                        return ResponseEntity.ok().body("User deleted");
                    }
                }
            }
            throw new UserNotFound(username);
        } catch (UserNotFound u) {
            return new ResponseEntity(u.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Update user", notes = "This can only be done by the logged in user.",
            response = User.class, tags={ "user", })
    @ApiResponses(value = { 
        @ApiResponse(code = 400, message = "Invalid user supplied", response = Void.class),
        @ApiResponse(code = 404, message = "User not found", response = Void.class) })
    @RequestMapping(value = "/users/{username}",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.PUT)
    ResponseEntity updateUser(@ApiParam(value = "name that need to be changed", required = true)
                    @PathVariable("username") String username,
                    @ApiParam(value = "Updated user object", required = true)
                    @RequestBody User body)
    throws Exception {
        try {
            User tempUser = getDatabase().getUser(username);
            if (tempUser == null) {
                logger.info("user not found");
                throw new UserNotFound(username);
            }
            String last, first, email, pass;
            first = (body.getFirstName() == null) ? tempUser.getFirstName() : body.getFirstName();
            last = (body.getLastName() == null) ? tempUser.getLastName() : body.getLastName();
            email = (body.getEmail() == null) ? tempUser.getEmail() : body.getEmail();
            pass = (body.getPassword() == null) ? tempUser.getPassword() : body.getPassword();
            User newUser = new User(
                    tempUser.getUserId(),
                    username,
                    first, last, email, pass  );
            UserEntity updatedUser = getDatabase().updateUser(newUser);
            return ResponseEntity.created(URI.create
                    ("users" + "/" + updatedUser.getUsername()))
                    .body(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}//class end
